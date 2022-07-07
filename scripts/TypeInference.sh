#!/bin/bash

#####################################
# chmod +x TypeInference.sh
# Run all benchmarks: ./TypeInference.sh all
# Run specific benchmark: ./TypeInference.sh <benchmark_name>

# For each benchmark, a config.txt file is required to indicate the index of entry method and taint infomation.

base_dir=$(pwd)/../
source_dir=$base_dir/src/type
benchmark_dir=$base_dir/experiments/type

cp $source_dir/TypeInference.java $source_dir/joana/api/joana.api/src/edu/kit/joana/api/example/ 
cd $source_dir/joana/api/joana.api/src/edu/kit/joana/api/example &&
javac -cp $source_dir/joana/dist/joana.ui.ifc.wala.cli/classes $source_dir/TypeInference.java &&
mv $source_dir/TypeInference.class $source_dir/joana/dist/joana.ui.ifc.wala.cli/classes/edu/kit/joana/api/example
cd $source_dir
# run all benchmarks
if [ "$1" == "all" ]; then
    for dir in `ls $benchmark_dir`
    do
        cd $benchmark_dir/$dir
        # skip all failed tests
        test -f log.txt
        if [ $? -eq 0 ]
        then
            echo "This benchmark: $dir failed, for detail please look at log.txt."
            continue
        fi
        test_dir=$(pwd)

        # get jar or class file
        test -f *.jar
        if [ $? -ne 0 ]
        then
            test_file=$(ls *.class)
        else
            test_file=$(ls *.jar)
        fi

        # read config file
        array=(0 0 0 0)
        i=0
        for line in `cat config.txt`
        do
            array[i]=$line
            i=`expr $i + 1`
        done

        if [ $i -eq 2 ]; then
            cd $source_dir/joana/api/joana.api/src
            java -cp $source_dir/joana/dist/joana.ui.ifc.wala.cli/classes edu.kit.joana.api.example.TypeInference $test_dir $test_file ${array[0]} ${array[1]}
        else
            cd $source_dir/joana/api/joana.api/src
            java -cp $source_dir/joana/dist/joana.ui.ifc.wala.cli/classes edu.kit.joana.api.example.TypeInference $test_dir $test_file ${array[0]} ${array[1]} ${array[2]}
        fi
    done
# run specific benchmarks
else
    cd $benchmark_dir/$1
    # skip all failed tests
    test -f log.txt
    if [ $? -eq 0 ]
    then
        echo "This benchmark: $1 failed, for detail please look at log.txt."
        exit 0
    fi
    test_dir=$(pwd)
    # get jar or class file
    test -f *.jar
    if [ $? -ne 0 ]
    then
        test_file=$(ls *.class)
    else
        test_file=$(ls *.jar)
    fi

    # read config file
    array=(0 0 0 0)
    i=0
    for line in `cat config.txt`
    do
        array[i]=$line
        i=`expr $i + 1`
    done

    if [ $i -eq 2 ]; then
        cd $source_dir/joana/api/joana.api/src
        java -cp $source_dir/joana/dist/joana.ui.ifc.wala.cli/classes edu.kit.joana.api.example.TypeInference $test_dir $test_file ${array[0]} ${array[1]}
    else
        cd $source_dir/joana/api/joana.api/src
        java -cp $source_dir/joana/dist/joana.ui.ifc.wala.cli/classes edu.kit.joana.api.example.TypeInference $test_dir $test_file ${array[0]} ${array[1]} ${array[2]}
    fi
fi

cd $base_dir/scripts