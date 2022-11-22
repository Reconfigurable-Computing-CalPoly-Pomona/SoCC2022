# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.19

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Disable VCS-based implicit rules.
% : %,v


# Disable VCS-based implicit rules.
% : RCS/%


# Disable VCS-based implicit rules.
% : RCS/%,v


# Disable VCS-based implicit rules.
% : SCCS/s.%


# Disable VCS-based implicit rules.
% : s.%


.SUFFIXES: .hpux_make_needs_suffix_list


# Command-line flag to silence nested $(MAKE).
$(VERBOSE)MAKESILENT = -s

#Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/local/bin/cmake

# The command to remove a file.
RM = /usr/local/bin/cmake -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /mnt/d/JI/HLS_HDL/ASCON/ascon/ascon-c-master

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /mnt/d/JI/HLS_HDL/ASCON/ascon/ascon-c-master/build

# Include any dependencies generated for this target.
include CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/flags.make

CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/tests/getcycles.c.o: CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/flags.make
CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/tests/getcycles.c.o: ../tests/getcycles.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/mnt/d/JI/HLS_HDL/ASCON/ascon/ascon-c-master/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/tests/getcycles.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/tests/getcycles.c.o -c /mnt/d/JI/HLS_HDL/ASCON/ascon/ascon-c-master/tests/getcycles.c

CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/tests/getcycles.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/tests/getcycles.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /mnt/d/JI/HLS_HDL/ASCON/ascon/ascon-c-master/tests/getcycles.c > CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/tests/getcycles.c.i

CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/tests/getcycles.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/tests/getcycles.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /mnt/d/JI/HLS_HDL/ASCON/ascon/ascon-c-master/tests/getcycles.c -o CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/tests/getcycles.c.s

# Object files for target getcycles_crypto_hash_asconhashv12_bi32
getcycles_crypto_hash_asconhashv12_bi32_OBJECTS = \
"CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/tests/getcycles.c.o"

# External object files for target getcycles_crypto_hash_asconhashv12_bi32
getcycles_crypto_hash_asconhashv12_bi32_EXTERNAL_OBJECTS =

getcycles_crypto_hash_asconhashv12_bi32: CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/tests/getcycles.c.o
getcycles_crypto_hash_asconhashv12_bi32: CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/build.make
getcycles_crypto_hash_asconhashv12_bi32: libcrypto_hash_asconhashv12_bi32.a
getcycles_crypto_hash_asconhashv12_bi32: CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/mnt/d/JI/HLS_HDL/ASCON/ascon/ascon-c-master/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking C executable getcycles_crypto_hash_asconhashv12_bi32"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/build: getcycles_crypto_hash_asconhashv12_bi32

.PHONY : CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/build

CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/cmake_clean.cmake
.PHONY : CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/clean

CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/depend:
	cd /mnt/d/JI/HLS_HDL/ASCON/ascon/ascon-c-master/build && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /mnt/d/JI/HLS_HDL/ASCON/ascon/ascon-c-master /mnt/d/JI/HLS_HDL/ASCON/ascon/ascon-c-master /mnt/d/JI/HLS_HDL/ASCON/ascon/ascon-c-master/build /mnt/d/JI/HLS_HDL/ASCON/ascon/ascon-c-master/build /mnt/d/JI/HLS_HDL/ASCON/ascon/ascon-c-master/build/CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/getcycles_crypto_hash_asconhashv12_bi32.dir/depend

