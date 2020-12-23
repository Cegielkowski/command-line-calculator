#!/usr/bin/env bash

rm -rf target
mkdir target
javac src/pkg/* -d target
java -cp target pkg.Main