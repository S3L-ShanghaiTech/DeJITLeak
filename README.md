# DeJITLeak
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

* DeJITLeak is developed and tested under Ubuntu 20.04 and JDK 8.
* Git (>= 2.16.2, version control)
* Apache Maven (>= 3.6.3, for Joana compilation)
* Apache Ant (>= 1.9.6, for Joana compilation)
* JDK8 (openjdk recommended)

## Structure

* src: contains both part of DeJITLeak
  - flow: flow analyser based on Joana
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

Then you can follow [Run DeJITLeak](#3-Run-DeJITLeak) for running DeJITLeak.

### Build from Scratch

#### **0. Install dependencies**

```bash
sudo apt-get update
sudo apt-get install git openjdk-8-jdk
sudo apt-get install maven ant
sudo apt-get install octave
sudo apt-get install python3 python3-pip
pip install pandas
sudo update-alternatives --config java # select java-8-openjdk
```

#### **1. Clone the DeJITLeak repository**

Currently, there are some issue with Joana's Git server, we need to disable the SSL verification.

```bash
GIT_SSL_NO_VERIFY=1 git clone --recurse-submodules https://github.com/LeoQ7/DeJITLeak
```

Download patched JVM from `https://drive.google.com/file/d/1vbXbooqhZtVSI_kQXidBogr0NnuJKerR/view?usp=sharing`, decompress and put it in `src` folder.

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

#### **3. Run DeJITLeak**

The following operations will all be running at the `DeJITLeak/scripts` directory.

Run type inference
```bash
./TypeInference.sh all
```

Test effectiveness of DeJITLeak
```bash
python3 eval.py # full benchmark, will take several days
python3 eval.py demo # demo benchmark (a subset), will take ~8 minutes
```

Analyse experiment results, columns of the output file represent `normal`, `NOJIT`, `DisableC2`, `MExclude`, `DeJITLeak`, `DeJITLeak_Light` respectively.
```bash
python3 analysis.py leakage 2>/dev/null # measure leakage
python3 analysis.py time 2>/dev/null # measure overhead
python3 analysis.py leakage demo 2>/dev/null # measure leakage for demo
python3 analysis.py time demo 2>/dev/null # measure overhead for demo
```

Note that for the demo benchmark, the results are not reliable. The reason is that the demo benchmark only executes each program 100 times while the full benchmark executes each program 1000 times to reduce the impact of the noise.

## Publication
```
@inproceedings{Qi2022dejitleak,
  title={DeJITLeak: Eliminating JIT-Induced Timing Side-Channel Leaks},
  author={Qi Qin, JulianAndres Jiyang, Fu song, Taolue Chen, Xinyu Xing},
  booktitle={{ESEC/FSE} '22: 30th {ACM} Joint European Software Engineering Conference and Symposium on the Foundations of Software Engineering, Singapore, November 14-16, 2022},
  year={2022}
}
```