# Small Neural Network

A small CPU implementation of a neural network.

## What is this?

A collaboration between Dalton Herrewynen and Christine Zhang on implementing a neural network in C++. The idea came from a Java project I did for one of my classes, the class built a very small neural network in Java. A mutual idea between Christine and birthed this project.

## Project outline

- Start by importing my old Java project files into a folder in this project
	- Convert the Java project as-is into a C++ version
	- Test it to see that it trains correctly
- Re-write for speed and efficiency
	- The original project is very inefficient because of the way the lab was designed
		- Single neurons as objects rather than in a matrix
		- Single threaded
		- Optimization was **not** a priority in the lab
		- "The lab should be readable even if it's slow, TensorFlow is what you would be using in the field anyway" - My Professor during the lab
	- C++ can be very fast
	- Speed is important for any more neurons to be added
- Stretch goal: Consider GPU acceleration

Note, this is a programming challenge, not a competitor to more established systems such as TensorFlow.

## Design

At least two libraries will be created. One library is a strict re-write of my original lab assignment, and will retain of its flawed design. The second library will be a more optimized replacement to the original as designed by myself and Christine. We may or may not create a third library with more optimized code that is GPU accelerated, this depends on if we both feel like doing so would be enjoyable.

### Deliverable 1: Lab re-write

A strict re-write of my Java lab. This version is basically a translation across languages with minimal changes to the design. I anticipate a slow performing library because we were discouraged from using matrices for the sake of readability. It was a proof of concept lab after all.

### Deliverable 2: Our own version

This is a version we will design together. It will be designed after we have completed the first deliverable and have agreed upon exactly how much time to spend on this upgraded version.

## Reasons

- Curiosity
	- I want to know how much effort it would take to have done the lab in C++
	- I also want to see what performance difference between the Java lab and the identical C++ version
	- I really want to know how much faster an optimized version will perform assuming both are on the exact same CPU
	- This is never going to perform as fast as GPU accelerated neural networks, but it's easier to see how a neural network works when the code is a simple CPU-only program
- A fun challenge
	- Designing the original was challenging
	- Building the upgraded version should also be a challenge, and challenges lead to success
- To show what we both know
	- Christine has more experience using machine learning libraries than I do, my training was more on the concepts than how to use any one library
	- I was required to design my own library from scratch for the lab, now I want to see how efficient I can make the lab assignment if I had to do it again
	- Collaboration with git is the best way to display and strengthen the ability to use version control systems
	- Building something from scratch is one of the best ways to show what has been learned

## Running

Use the included makefile.

```shell
make run
```