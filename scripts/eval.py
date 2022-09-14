import os
import sys
import string
import random
import re
from tqdm import tqdm
import numpy as np
import pandas as pd
import pickle

JAVA_PATH =  os.path.join(os.path.dirname(os.path.abspath(__file__)), "../src/jvm/jdk/bin") 
BENCHMARK_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "../experiments/protection")
RESULTS_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "../experiments/results")
ALL_FLAG = "-cp .:../lib/* -Xbatch -XX:-BackgroundCompilation -XX:CICompilerCount=2"

N = 1000

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

is_demo = False
if len(sys.argv) > 1 and sys.argv[1] == 'demo':
    BENCHMARKS = ['blazer_array_safe', 'blazer_straightline_safe', 'themis_jdk_safe']
    N = 100
    is_demo = True
elif len(sys.argv) > 2 and sys.argv[1] == 'single':
    BENCHMARKS = [sys.argv[2]]


for benchmark in BENCHMARKS:
    os.chdir(f'{BENCHMARK_DIR}/{benchmark}/bin')

    nodefense_false = []
    nodefense_true = []
    defense_false = []
    defense_true = []
    defense1_false = []
    defense1_true = []
    noc2_false = []
    noc2_true = []
    nojit_false = []
    nojit_true = []
    mexclude_false = []
    mexclude_true = []

    for _ in tqdm(range(N), desc=f'{benchmark:40s}'):

        res = os.popen(f'{JAVA_PATH}/java {ALL_FLAG} Attack -1').read()
        tt = int(re.search(r'cost: (\d+)', res).groups()[0])
        nodefense_false.append(tt)

        res = os.popen(f'{JAVA_PATH}/java {ALL_FLAG} Attack 1').read()
        tt = int(re.search(r'cost: (\d+)', res).groups()[0])
        nodefense_true.append(tt)

        res = os.popen(f'{JAVA_PATH}/java {ALL_FLAG} -XX:TieredStopAtLevel=3 Attack -1').read()
        tt = int(re.search(r'cost: (\d+)', res).groups()[0])
        noc2_false.append(tt)

        res = os.popen(f'{JAVA_PATH}/java {ALL_FLAG} -XX:TieredStopAtLevel=3 Attack 1').read()
        tt = int(re.search(r'cost: (\d+)', res).groups()[0])
        noc2_true.append(tt)

        res = os.popen(f'{JAVA_PATH}/java {ALL_FLAG} -Djava.compiler=NONE -Xint Attack -1').read()
        tt = int(re.search(r'cost: (\d+)', res).groups()[0])
        nojit_false.append(tt)

        res = os.popen(f'{JAVA_PATH}/java {ALL_FLAG} -Djava.compiler=NONE -Xint Attack 1').read()
        tt = int(re.search(r'cost: (\d+)', res).groups()[0])
        nojit_true.append(tt)

        res = os.popen(f'{JAVA_PATH}/java {ALL_FLAG} -XX:CompileCommandFile=compileOracle.txt Attack -1').read()
        tt = int(re.search(r'cost: (\d+)', res).groups()[0])
        defense_false.append(tt)

        res = os.popen(f'{JAVA_PATH}/java {ALL_FLAG} -XX:CompileCommandFile=compileOracle.txt Attack 1').read()
        tt = int(re.search(r'cost: (\d+)', res).groups()[0])
        defense_true.append(tt)

        res = os.popen(f'{JAVA_PATH}/java {ALL_FLAG} -XX:CompileCommandFile=compileOracle_minus.txt Attack -1').read()
        tt = int(re.search(r'cost: (\d+)', res).groups()[0])
        defense1_false.append(tt)

        res = os.popen(f'{JAVA_PATH}/java {ALL_FLAG} -XX:CompileCommandFile=compileOracle_minus.txt Attack 1').read()
        tt = int(re.search(r'cost: (\d+)', res).groups()[0])
        defense1_true.append(tt)

        res = os.popen(f'{JAVA_PATH}/java {ALL_FLAG} -XX:CompileCommandFile=compileOracle_mexclude.txt Attack -1').read()
        tt = int(re.search(r'cost: (\d+)', res).groups()[0])
        mexclude_false.append(tt)

        res = os.popen(f'{JAVA_PATH}/java {ALL_FLAG} -XX:CompileCommandFile=compileOracle_mexclude.txt Attack 1').read()
        tt = int(re.search(r'cost: (\d+)', res).groups()[0])
        mexclude_true.append(tt)

    os.chdir('../..')
    data = {
        'True w/o Defense': nodefense_true,
        'False w/o Defense': nodefense_false,
        'True w/o JIT': nojit_true,
        'False w/o JIT': nojit_false,
        'True w/o C2': noc2_true,
        'False w/o C2': noc2_false,
        'True w/ Defense': defense_true,
        'False w/ Defense': defense_false,
        'True w/ Defense1': defense1_true,
        'False w/ Defense1': defense1_false,
        'True w/ mexclude': mexclude_true,
        'False w/ mexclude': mexclude_false}
    df = pd.DataFrame(data)
    if is_demo:
        pickle.dump(df, open(f'{RESULTS_DIR}/demo_{benchmark}.pkl', 'wb'))
    else:
        pickle.dump(df, open(f'{RESULTS_DIR}/{benchmark}.pkl', 'wb'))
