#!/bin/sh

if [ $# -lt 2 ];then
    echo "USAGE:"
    echo "release.sh release_version new_version"
    echo "for example:"
    echo "release.sh 0.9 0.9.1-SNAPSHOT"
    exit 1
fi

# replaces the first occurence of <version>...</version> in every pom.xml with what is given in the first argument
replace_poms_versions() { 
    sed -i "0,/<version>.*<\/version>/s/<version>.*<\/version>/<version>$1<\/version>/" pom.xml **/pom.xml    
}

replace_poms_versions $1
git add pom.xml **/pom.xml
git commit -m "releasing version $1" 
git tag -a v$1 -m "version $1"
git push
git push --tags

if [ -n $2 ];then
    replace_poms_versions $2
fi

