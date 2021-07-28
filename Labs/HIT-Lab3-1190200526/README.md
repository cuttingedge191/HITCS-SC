# HIT-Lab3-1190200526

[![Java CI with Maven](https://github.com/ComputerScienceHIT/HIT-Lab3-1190200526/actions/workflows/maven.yml/badge.svg)](https://github.com/ComputerScienceHIT/HIT-Lab3-1190200526/actions/workflows/maven.yml)

## 开发环境

### IDE

Eclipse IDE for Java Developers  
Version:2021-06(4.20.0)  
Build id:20210612-2011

### Java

JDK 8 + JRE 1.8.0_281

## 关于文件数据读入

- 程序读取时使用相对路径，例如`txt/test1.txt`
- 测试文件中存在无法通过正则表达式`\s*`识别的空白字符`'　'`，已经被替换为两个空格

## 测试说明

- 项目导入Eclipse IDE应该可以直接进行运行和测试
- APP设计为命令行程序，测试时建议使用Eclipse Console

## 关于lib文件夹

lib文件夹下为进行JUnit测试时所需的jar包，若使用非Eclipse环境运行Junit测试（`/test`文件夹下为测试代码）需导入。

## 附加说明

- 项目master分支通过GitHub Actions在线构建和测试
- change分支下仅进行了实验3.8节——”应对面临的新变化“这一部分的内容，实验报告在此分支下并不是最新，相关测试用例也未重新编写（非实验要求），如果在此分支下执行测试用例会测试失败
- master分支下包含实验主体任务的全部内容和最终的实验报告，测试用例应该可以全部通过

[![Work in Repl.it](https://classroom.github.com/assets/work-in-replit-14baed9a392b3a25080506f3b7b6d57f295ec2978f6f33ec97e36a161684cbe9.svg)](https://classroom.github.com/online_ide?assignment_repo_id=4850134&assignment_repo_type=AssignmentRepo)

