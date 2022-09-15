# DeJITLeak [![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.7034134.svg)](https://doi.org/10.5281/zenodo.7034134)
This repository provides the tool for the paper "DeJITLeak: Eliminating JIT-Induced Timing Side-Channel Leaks" accepted at ECSE/FSE 2022.

Table of Contents
=================
* [Requirements](#requirements)
* [Structure](#structure)
* [Getting Started Instructions](#getting-started-instructions)
  * [Docker Setup](#docker-setup)
  * [Build from Scratch](#build-from-scratch)
* [Publication](#publication)

## Requirements

* OS: 
  - DeJITLeak is developed and tested under Ubuntu 20.04 x86_64. 
  - Other Debian or Debian-based Linux systems may also work. We tested it on Ubuntu 18.04 and Ubuntu 22.04.
* Git (>= 2.16.2, version control)
* Apache Maven (>= 3.6.3, for Joana compilation)
* Apache Ant (>= 1.9.6, for Joana compilation)
* JDK8 (openjdk recommended)
* Python3 (>= 3.7, with pandas and tqdm installed, for running the evaluation scripts)
* Octave (>= 6.2.0, for measuring leakage)
* Docker (optional, for [Docker Setup](#docker-setup) only)
* gdown (optional, for downloading JVM from Google Drive in command line)

## Structure

* src: contains both part of DeJITLeak
  - type: type inference based on Joana
  - jvm: patched openjdk for fine-grained JIT control
  - side-channel-analyser: script for quantifying leakage

* experiments: contains all data and results of experiments
  - type: benchmarks for type inference
  - protection: benchmarks for protection
  - results: experiment results in pickle format

* scripts: contains all scripts for experiments
  - analysis.py: script for analysing experiment results
  - eval.py: run experiments ( will take several days for full evaluation )
  - parse.py: script for generating compiler oracle according to taint analysis result
  - TypeInference.sh: script for running type inference

## Getting Started Instructions

### Docker Setup

For convenience, we provide a docker image for the DeJITLeak.

```bash
docker pull leoq7/dejitleak:latest
docker run -itd --name dejitleak leoq7/dejitleak:latest
docker exec -it dejitleak /bin/bash
cd /DeJITLeak/scripts
```

Then you can follow [Run the Evaluation](#4-Run-the-Evaluation) for running the evaluation.

### Build from Scratch

#### **0. Install dependencies**

```bash
sudo apt-get update
sudo apt-get install git openjdk-8-jdk
sudo apt-get install maven ant
sudo apt-get install octave
sudo apt-get install python3 python3-pip
pip3 install pandas tqdm
pip3 install gdown # optional
sudo update-alternatives --config java # select java-8-openjdk
```

#### **1. Clone the DeJITLeak repository**

Currently, there are some issue with Joana's Git server, we need to disable the SSL verification.

```bash
GIT_SSL_NO_VERIFY=1 git clone --recurse-submodules https://github.com/LeoQ7/DeJITLeak
```

Download patched JVM from `https://drive.google.com/file/d/1vbXbooqhZtVSI_kQXidBogr0NnuJKerR/view?usp=sharing`, decompress and put it in `DeJITLeak/src` folder.

```bash
gdown 1vbXbooqhZtVSI_kQXidBogr0NnuJKerR # you can also download the file directly
tar -xvf jvm.tar.gz -C DeJITLeak/src/
```

#### **2. Build Joana**

```bash
cd DeJITLeak/src/type/joana
GIT_SSL_NO_VERIFY=1 ./setup_deps # ~5 min
ant # ~3 min
```

#### **3. Test DeJITLeak**

The following operations will all be running at the `DeJITLeak/scripts` directory.

Test type inference
```bash
./TypeInference.sh blazer_array_safe
```
The output should be similar to the following:
```
Searching for methods in '/DeJITLeak/experiments/type/blazer_array_safe/blazer_array.jar'...
        found 'MoreSanity.<init>()V'
        found 'MoreSanity.array_safe([II)Z'
        found 'MoreSanity.array_unsafe([II)Z'
done.

Setting up analysis scope... (from jar stream stubs/jdk-1.5-with-stubs.jar) done.
Creating class hierarchy... (5027 classes) done.
Setting up entrypoint MoreSanity.array_safe([II)Z... done.
Building system dependence graph...
        callgraph: 2 nodes and 1 edges
        interproc exception analysis...         intraproc: calls.mergeable..clinit.statics.heap(if,adj,df,reg).misc.killdef.interference.convert.summary..
done.
Time needed: 1071ms - Memory: 124M used.

Taint analysis takes: 16 ms.
Illicit Flow From SDGNode 5 At SDGNode 6, visible for low
Illicit Flow From SDGNode 5 At SDGNode 10, visible for low
Illicit Flow From SDGNode 5 At SDGNode 20, visible for low
Illegal Flow from parameter <param> 2 of method boolean MoreSanity.array_safe(int[], int) to (boolean MoreSanity.array_safe(int[], int):31) if (v11 >= v5) goto 36, visible for low
Illegal Flow from parameter <param> 2 of method boolean MoreSanity.array_safe(int[], int) to (boolean MoreSanity.array_safe(int[], int):10) if (v17 < #(0)) goto 23, visible for low
Illegal Flow from parameter <param> 2 of method boolean MoreSanity.array_safe(int[], int) to (boolean MoreSanity.array_safe(int[], int):1) if (p2 $taint  >= #(0)) goto 5, visible for low
```

At first, it will search for all the methods in the given jar file. Then it will set up the analysis by setting up the entry point according to the `config.txt` file. Then it will build the system dependence graph and output the time and memory usage. Finally, it will perform the taint analysis and output some illicit flows if any. Each illicit flow represents a potential leaky branch that needs to be protected. These branches are also logged in `if_branch.txt`. Moreover, the methods invoked in these branches are logged in `call.txt`. 

For each branch, its pseudocode, the method it is in, and its byte code index are recorded.Branches in `if_branch.txt` all have high security level H and need to be protected, while branches contained in `if_branch_prev.txt` but not in `if_branch.txt` all have low security level L. If one wants to understand `if_branch.txt` in detail, one can use [the visualisation tool](https://pp.ipd.kit.edu/projects/joana/joana.ui.ifc.sdg.graphviewer.jar) provided by Joana to visualise the generated `SDGFile.pdg` and cross-reference it with `if_branch.txt`.

Test effectiveness of DeJITLeak
```bash
cd ../experiments/protection/blazer_array_safe/bin
../../../../src/jvm/jdk/bin/java -cp .:../lib/* -Xbatch -XX:-BackgroundCompilation -XX:CICompilerCount=2 Attack -1
../../../../src/jvm/jdk/bin/java -cp .:../lib/* -Xbatch -XX:-BackgroundCompilation -XX:CICompilerCount=2 Attack 1
../../../../src/jvm/jdk/bin/java -cp .:../lib/* -Xbatch -XX:-BackgroundCompilation -XX:CICompilerCount=2 -XX:CompileCommandFile=compileOracle.txt Attack -1
../../../../src/jvm/jdk/bin/java -cp .:../lib/* -Xbatch -XX:-BackgroundCompilation -XX:CICompilerCount=2 -XX:CompileCommandFile=compileOracle.txt Attack 1
```
The `1` and `-1` in the arguments are the input to the program, which will be used to decide which branch to execute.

For each run, the output should be two lines, the first line is the execution time of the evaluated method in nanoseconds, and the second line is the output of the evaluated method.

The second and third commands tested JVM without our mitigation, the cost should be ~200 ns and >10000 ns respectively, indicating that there is a large timing side-channel.

The fourth and fifth commands tested JVM with our mitigation, both cost should be ~200 ns, indicating that our mitigation is effective. 

As mentioned in the paper, our mitigation leverages three compile commands `exclude`, `dontinline` and `dontprune` to enforce constant time execution. For more details, please refer to Section 5 in the paper. A prototype implementation of `dontprune` is provided in `src/jvm_impl/dontprune.diff`.

#### **4. Run the Evalution**

The following operations will all be running at the `DeJITLeak/scripts` directory.

Run type inference
```bash
./TypeInference.sh all
```

Test effectiveness of DeJITLeak
```bash
python3 eval.py # full benchmark, will take several days
python3 eval.py demo # demo benchmark (a subset), will take ~8 minutes
python3 eval.py single blazer_array_safe # run single benchmark (blazer_array_safe)
```

Analyse experiment results, columns of the output file represent `normal`, `NOJIT`, `DisableC2`, `MExclude`, `DeJITLeak`, `DeJITLeak_Light` respectively (Table 2 in the paper).
Different from in Table 2, the time output from `analysis.py` is the execution time in nanosecond for each benchmark.
```bash
python3 analysis.py leakage # measure leakage
python3 analysis.py time # measure overhead
python3 analysis.py leakage demo # measure leakage for demo subset
python3 analysis.py time demo # measure overhead for demo subset
python3 analysis.py leakage single blazer_array_safe # measure leakage for single benchmark (blazer_array_safe)
python3 analysis.py time single blazer_array_safe # measure overhead for single benchmark (blazer_array_safe)
```

Note that for the demo benchmark, the results are not reliable. The reason is that the demo benchmark only executes each program 100 times while the full benchmark executes each program 1000 times to reduce the impact of the noise.

## Troubleshooting

1. If you encounter an error like `/bin/javah: not found` installing Joana, please run `sudo update-alternatives --install /usr/bin/javah javah <path to javah> 1`.

2. `Command 'gdown' not found`. This may occur if the path to `gdown` is not in your `PATH` environment variable. You can fix this by running `export PATH=$PATH:~/.local/bin` or adding this line to your `~/.bashrc` file. You can also downloaded the file directly using a browser instead of `gdown`.

3. If you have any other problems, feel free to open an issue on GitHub or email me directly.

## Publication

If you find this repository useful, please consider citing our paper.

```
@inproceedings{Qi2022dejitleak,
  title={DeJITLeak: Eliminating JIT-Induced Timing Side-Channel Leaks},
  author={Qi Qin, JulianAndres Jiyang, Fu song, Taolue Chen, Xinyu Xing},
  booktitle={{ESEC/FSE} '22: 30th {ACM} Joint European Software Engineering Conference and Symposium on the Foundations of Software Engineering, Singapore, Singapore, November 14-18, 2022},
  year={2022}
}
```
