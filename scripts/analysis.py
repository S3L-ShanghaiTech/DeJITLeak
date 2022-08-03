import pickle
import os
import sys

MEASURE = 'leakage'
if sys.argv[1] == 'time':
    MEASURE = 'time'

CWD = os.getcwd()
ANALYSER_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), '../src/side-channel-analyser')
RESULTS_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), '../experiments/results')

BENCHMARKS = [
    'apache_ftpserver_clear_safe',
    'apache_ftpserver_md5_safe',
    'apache_ftpserver_salted_safe',
    'apache_ftpserver_stringutils_safe',
    'github_authmreloaded_safe',
    'blazer_array_safe',
    'blazer_gpt14_safe',
    'blazer_k96_safe',
    'blazer_k96_patched_safe',
    'blazer_login_safe',
    'blazer_loopandbranch_safe',
    'blazer_modpow1_safe',
    'blazer_modpow1_patched_safe',
    'blazer_modpow2_safe',
    'blazer_modpow2_patched_safe',
    'blazer_passwordEq_safe',
    'blazer_sanity_safe',
    'blazer_straightline_safe',
    'blazer_unixlogin_safe',
    'themis_boot-stateless-auth_safe',
    'themis_jdk_safe',
    'themis_jetty_safe',
    'themis_orientdb_safe',
    'themis_picketbox_safe',
    'themis_spring-security_safe'
]

if len(sys.argv) > 2 and sys.argv[2] == 'demo':
    BENCHMARKS = ['demo_blazer_array_safe', 'demo_blazer_straightline_safe', 'demo_themis_jdk_safe']

def measure_leakage(data):
    data.rename(columns={data.columns[0]: True,
                         data.columns[1]: False}, inplace=True)

    header = '''# name: data
# type: matrix
# rows: {}
# columns: 2
1 0
0.5 0.5
'''
    output = ''
    # remove outliers
    m1, m2 = data.median()
    cnt = 0
    for t, f in zip(data[True], data[False]):
        if 0.75*m1 < t < 1.25*m1 and 0.75*m2 < f < 1.25*m2:
            output += f'{t} {f}\n'
            cnt += 1
    with open(f'/tmp/octave.data', 'w') as f:
        f.write(header.format(cnt+2) + output)

    os.chdir(ANALYSER_DIR)
    leakage = os.popen(
        f'octave --quiet leakage /tmp/octave.data').read().split('=')[1].strip()
    return float(leakage)

def measure_time(data):
    data.rename(columns={data.columns[0]: True,
                         data.columns[1]: False}, inplace=True)
    
    m1, m2 = data.median()
    cnt = 0
    s1 = 0
    s2 = 0
    # remove outliers
    for t, f in zip(data[True], data[False]):
        if 0.75*m1 < t < 1.25*m1 and 0.75*m2 < f < 1.25*m2:
            s1 += t
            s2 += f
            cnt += 1
    
    return int(round(min(s1/cnt, s2/cnt)))

if MEASURE == 'leakage':
    measure = measure_leakage
elif MEASURE == 'time':
    measure = measure_time
else:
    raise ValueError(f'Unknown measure metric: {MEASURE}')

for benchmark in BENCHMARKS:
    if not os.path.exists(f'{RESULTS_DIR}/{benchmark}.pkl'):
        print(benchmark, 'not found')
        continue
    df = pickle.load(open(f'{RESULTS_DIR}/{benchmark}.pkl', 'rb'))

    df_nodefense = df[['True w/o Defense', 'False w/o Defense']]
    leakage_nodefense = measure(df_nodefense)
    df_noJIT = df[['True w/o JIT', 'False w/o JIT']]
    leakage_noJIT = measure(df_noJIT)
    df_noC2 = df[['True w/o C2', 'False w/o C2']]
    leakage_noC2 = measure(df_noC2)
    df_mexclude = df[['True w/ mexclude', 'False w/ mexclude']]
    leakage_mexclude = measure(df_mexclude)
    df_defense = df[['True w/ Defense', 'False w/ Defense']]
    leakage_defense = measure(df_defense)
    df_defense1 = df[['True w/ Defense1', 'False w/ Defense1']]
    leakage_defense1 = measure(df_defense1)

    print(benchmark, leakage_nodefense, leakage_noJIT, leakage_noC2, leakage_mexclude, leakage_defense, leakage_defense1)

os.chdir(CWD)