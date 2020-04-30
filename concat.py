#!/usr/bin/python
# I use semicolons to keep in good habit, they are certainly not required
import sys;
import os;
import os.path;

helpmsg = "USAGE: (python) concat.py -o foo8.bin foo.bin_0 foo.bin_1...";

bankswritten = 0;
# Set outfile as an unassigned global variable, and set output to false
outfile = None;
output = False;

def add8k(infile):
# Use the global outfile because why not
    global outfile;
    global bankswritten;
    bankswritten += 1
# i contains the size of each bank, 0x2000 = 8kb
    i = 0x2000
    data = infile.read();
    for byte in data:
# infile.read doesn't return an array of bytes, weirdly enough...
        outfile.write(byte.to_bytes(1, byteorder="little"));
        i -= 1;
    while i:
# Pad the data with 0s
        outfile.write(0x00.to_bytes(1, byteorder="little"));
        i -= 1;

def pad8k():
# Use the global outfile because why not
    global outfile;
    global bankswritten;
    bankswritten += 1
# i contains the size of each bank, 0x2000 = 8kb
    i = 0x2000
    while i:
# Pad the data with 0s
        outfile.write(0x00.to_bytes(1, byteorder="little"));
        i -= 1;

def isPowerOf2(num):
    j = 0;
    while(num > 0):
        if((num & 1) > 0):
            j += 1;
        num >>= 1;
    if(j == 1):
        return True;
    else:
        return False;

# Handle all arguments except the first one(the program name)
for arg in sys.argv[1:]:
# If the -o flag has been set...
    if output == True:
        if os.path.isfile(arg):
            os.remove(arg);
        outfile = open(arg, "wb");
        output = False;
# If the argument is a -o flag...
    elif arg == "-o":
        output = True;
# If the argument is a -h or --help flag...
    elif arg == "-h" or arg == "--help":
        print(helpmsg);
        exit();
# If none of the above, treat the argument as a bank binary
    else:
        if outfile == None:
            print(helpmsg);
            exit();
        else:
            infile = open(arg, "rb");
            add8k(infile);

while(isPowerOf2(bankswritten) == False):
    pad8k();
