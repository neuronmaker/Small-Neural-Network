cmake_minimum_required(VERSION 3.24)
project(SmallNeuralNetwork)

set(CMAKE_CXX_STANDARD 17)
set(APP_ROOT .)
#set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR})
#set(EXECUTABLE_OUTPUT_PATH ./build)
include_directories(${APP_ROOT})

set(
		SOURCE_FILES
		main.cpp
)

add_executable(neuralnetwork ${SOURCE_FILES})
