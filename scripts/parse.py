import sys
import os
import glob
import logging
import coloredlogs
import re

CWD = os.getcwd()
BENCHMARK_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', 'experiments', 'protection')
TYPE_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', 'experiments', 'type')

defense_logger = logging.getLogger('DefenseGenerator')
# validate_logger = logging.getLogger('DefenseValidator')

coloredlogs.DEFAULT_FIELD_STYLES = {'asctime': {'color': 'green'}, 'hostname': {'color': 'magenta'},
                                        'levelname': {'bold': True, 'color': 'black'},
                                        'name': {'color': 'cyan', 'bold': True},
                                        'programname': {'color': 'blue'}, 'username': {'color': 'yellow'}}
coloredlogs.install(level='DEBUG', fmt="%(asctime)s %(name)-15s %(levelname)-10s | %(message)s")

testcases = [
    'apache_ftpserver_clear_safe',
    'apache_ftpserver_md5_safe',
    'apache_ftpserver_salted_safe',
    'apache_ftpserver_stringutils_safe',
    'github_authmreloaded_safe',
    'blazer_array_safe',
    'blazer_gpt14_safe',
    'blazer_k96_safe',
    'blazer_login_safe',
    'blazer_loopandbranch_safe',
    'blazer_modpow1_safe',
    'blazer_modpow2_safe',
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

defense_logger.info(f"Found {len(testcases)} safe benchmarks.")

os.chdir(BENCHMARK_DIR)

success = []
for testcase in testcases:
    joana_path = os.path.join(TYPE_DIR, testcase)
    call_file = os.path.join(joana_path, 'call.txt')
    if not os.path.exists(call_file):
        defense_logger.error(f'TypeInference failed for {testcase}')
        continue
    compiler_oracle = ''
    compiler_oracle_minus = ''
    compiler_oracle_mexclude = ''
    with open(call_file, 'r') as f:
        content = f.readlines()

    exclude_methods = set()

    oracle = ''
    for line in content:
        method_name, sig = line.strip().split('(')
        sig = '(' + sig
        exclude_methods.add(method_name)
        method_name = method_name.replace('.', '/', method_name.count('.')-1)

        sig = sig.replace('.','/')
        compiler_oracle += f'dontinline {method_name}, {sig}\n'
        compiler_oracle_minus += f'dontinline {method_name}\n'
        compiler_oracle += f'exclude {method_name}, {sig}\n'
        
        # compiler_oracle += f'dontinline {method_name}\n'
        # compiler_oracle += f'exclude {method_name}\n'


    branch_file = os.path.join(joana_path, 'if_branch.txt')
    with open(branch_file, 'r') as f:
        content1 = f.readlines()

    dont_prune = dict()
    for idx in range(0, len(content1), 3):
        line = content1[idx+1].strip()
        method_name, sig = line.strip().split('(')
        sig = '(' + sig
        if line in dont_prune.keys():
            dont_prune[line].append(int(content1[idx+2]))
        else:
            dont_prune[line] = [int(content1[idx+2])]

    for line in dont_prune.keys():
        method_name, sig = line.strip().split('(')
        sig = '(' + sig
        already_exclude = method_name in exclude_methods
        method_name = method_name.replace('.', '/', method_name.count('.')-1)
        if not already_exclude:
            compiler_oracle += f'dontprune {method_name}, {sig}\n'
            compiler_oracle_mexclude += f'exclude {method_name}, {sig}\n'
        compiler_oracle_minus += f'dontprune {method_name}, {sig}\n'


    for line, lineNum in dont_prune.items():
        fname = f"noprune_{line.replace('/','_').replace('.','_')}"
        fname = os.path.join(testcase, 'bin', fname)
        defense_logger.debug(f'Generate noprune file {fname}')
        with open(fname, 'w') as f:
            for line in lineNum:
                f.write(f'{line}\n')
    with open(os.path.join(testcase, 'bin', 'compileOracle.txt'), 'w') as f:
        f.write(compiler_oracle)
    with open(os.path.join(testcase, 'bin', 'compileOracle_minus.txt'), 'w') as f:
        f.write(compiler_oracle_minus)
    with open(os.path.join(testcase, 'bin', 'compileOracle_mexclude.txt'), 'w') as f:
        f.write(compiler_oracle_mexclude)
    defense_logger.info(f'Generated {len(exclude_methods)} excludes & {len(dont_prune)} noprunes for {testcase}')
    success.append(testcase)

defense_logger.info(f'Successfully generated compiler oracle for {len(success)} benchmarks')

os.chdir(CWD)

# JAVA_PATH = "../../../dejitleak/jvm/jdk/bin"
# ALL_FLAG = "-cp .:../lib/* -Xbatch -XX:-BackgroundCompilation -XX:CICompilerCount=2"

# for test in testcases:
#     os.chdir(f'{test}/bin')
#     res = os.popen(f'{JAVA_PATH}/java {ALL_FLAG} -XX:CompileCommandFile=compileOracle.txt Attack -1').read()
#     if 'cost: ' in res and 'answer: ' in res:
#         validate_logger.info(f'Compiler oracle for {test} passed the validation')
#     else:
#         validate_logger.error(f'Wrong compiler oracle for {test}!')
#     os.chdir('../..')
