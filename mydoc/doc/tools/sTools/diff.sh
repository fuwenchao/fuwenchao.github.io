#!/bin/sh
# @AUTHOR   wenchaofu
# @DATE     2019年8月6日18:42:14
# @VERSION  1.0
# @DESC     文件比对


## 参数检查
if [ $# -ne 3 ];then
   echo "Usage: $0 <DEST_DIR> <SRC_DIR> <DIFF_DIR>"
   exit 1
fi

DEST_DIR=$1
DEST_DIR_PURE=`echo ${DEST_DIR}|sed 's#/$##'`
SRC_DIR=$2
SRC_DIR_PURE=`echo ${SRC_DIR}|sed 's#/$##'`
DIFF_DIR=$3
DIFF_DIR_PURE=`echo ${DIFF_DIR}|sed 's#/$##'`

function ckeckDirExist(){
  dir=$1
  [ -d $dir ] || {
    echo "$dir not exist,exit..."
    exit
  }
}


function ckeckDirNotExist(){
  dir=$1
  [ -d $dir ] && {
    echo "$dir exist,exit..."
    exit
  }
}

function diffDir(){

  diff -rq  ${DEST_DIR} ${SRC_DIR}>/tmp/diff.txt

  while read line
  do
    echo "\$line is $line...."
    #文件不一致
    fromStr=`echo $line|awk '/^Files/{print $2}'`
    [ $? -ne 0 ] && {
      echo "cmd1 failed..."
      exit 1
    }
    #目录不存在或者文件不存在
    if [ -z $fromStr ];then
      #fromStr=`echo $line|awk -F" in " '/^Only in d-dist\//{print $2}'|sed -e "s#/: #/#" -e "s#: #/#"`
      fromStr=`echo $line|grep ^"Only in ${DEST_DIR_PURE}\/"|awk -F" in " '{print $2}'|sed -e "s#/: #/#" -e "s#: #/#"`
      [ $? -ne 0 ] && {
        echo "cmd2 failed..."
        exit 1
      }
    fi
    echo "\$fromStr is $fromStr"
    [ -z $fromStr ] && {
      echo "nothing to mv....."
      continue
    }
    diffDirStr=`dirname $fromStr|sed "s#^${DEST_DIR_PURE}#${DIFF_DIR_PURE}#"`
    echo $diffDirStr
    [ $? -ne 0 ] && {
      echo "cmd3 failed..."
      exit 1
    }
    basestr=`basename $fromStr`

    [ -d $diffDirStr ] ||{
      mkdir -p $diffDirStr
    }

    echo "cp -r $fromStr $diffDirStr"
    cp -r $fromStr $diffDirStr
    [ $? -ne 0 ] && {
      echo "cmd4 failed..."
      exit 1
    }

  done</tmp/diff.txt

}

function main(){
  
  ckeckDirExist ${DEST_DIR}

  ckeckDirExist ${SRC_DIR}

  ckeckDirNotExist ${DIFF_DIR}

  diffDir

  echo "********************success********************"

}


main