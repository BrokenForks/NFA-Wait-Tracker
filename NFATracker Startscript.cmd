:: Quick and dirty Windows Command Processor script to compile and launch the NFA Wait Tracker
:: It probably doesn't work on your system, but if it does, more power to you.
:: To even have a chance you need to download and install Java Development Kit (JDK)
:: And make sure javac (The compiler) is added to your PATH system variable.
:: This file also needs to be in the same directory as NFAWait.java to work properly.

::Disable console echo
@echo off

::If the compiled version of NFA Wait Tracker does not exist, run the compiler to create it.
IF NOT EXIST NFAWait.class (javac NFAWait.java)

::Run the compiled version of NFA Wait Tracker. 
java NFAWait
