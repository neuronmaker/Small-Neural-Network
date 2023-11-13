SHELL=/bin/bash
CXX = g++
OBJECTS = build/object
DEBUG_EXEC = build/debug
EXECUTABLE_NAME = NeuralNetwork

run: compile
	@./$(DEBUG_EXEC)/$(EXECUTABLE_NAME)

setup:
	@echo "Creating build folders"
	@mkdir -p $(OBJECTS)
	@mkdir -p $(DEBUG_EXEC)

clean:
	@echo "Destroying build area"
	@rm -r build

compile: main.o
	@echo "Compiling objects"
	@$(CXX) -o $(DEBUG_EXEC)/$(EXECUTABLE_NAME) $(OBJECTS)/*.o

main.o: setup
	@$(CXX) -c main.cpp -o $(OBJECTS)/main.o