: Quick and dirty script to compile and launch the NFA Wait Tracker
: It probably doesn't work on your system, but if it does, more power to you.
: To even have a chance you need to download and install Java Development Kit (JDK)
: And make sure javac (The compiler) is added to your PATH system variable.

@echo off

IF NOT EXIST NFAWait.class (javac NFAWait.java)

java NFAWait