#include <iostream>
#ifdef ANOTHER_CONFIG_H
#include "another.config.h"
#else 
#include "config.h"
#endif

using namespace std;

int slow_compute() {
	int sum = 0;
	for (int i = 1; i <= 1024; i++) {
		for (int j = 1; j <= 1024; j++) {
			for (int k = 1; k <= 1024; k++) {
				sum += i * j + j * k + i * k;
			}
		}
	}
	return sum;
}

void lambda() {
	auto f = [=](){ 
		cout << "lambda" << endl << endl; 
	};
	f();
}

string conditional_compile() {
#ifdef IS_WINDOWS
  return string("Hello from Windows");
#elif IS_LINUX
  return string("Hello from Linux");
#elif IS_MACOS
  return string("Hello from MacOS");
#else
  return string("Hello from an Unknown System");
#endif
}

void processor_info() {
  cout << "Number of logical cores: " << NUMBER_OF_LOGICAL_CORES << endl;
  cout << "Number of physical cores: " << NUMBER_OF_PHYSICAL_CORES << endl;

  cout << "Total virtual memory in megabytes: " << TOTAL_VIRTUAL_MEMORY
            << endl;
  cout << "Available virtual memory in megabytes: " << AVAILABLE_VIRTUAL_MEMORY
            << endl;
  cout << "Total physical memory in megabytes: " << TOTAL_PHYSICAL_MEMORY
            << endl;
  cout << "Available physical memory in megabytes: "
            << AVAILABLE_PHYSICAL_MEMORY << endl;

  cout << "Processor is 64Bit: " << IS_64BIT << endl;

  cout << "Processor has floating point unit: " << HAS_FPU << endl;
  cout << "Processor supports MMX instructions: " << HAS_MMX << endl;
  cout << "Processor supports Ext. MMX instructions: " << HAS_MMX_PLUS
            << endl;
  cout << "Processor supports SSE instructions: " << HAS_SSE << endl;
  cout << "Processor supports SSE2 instructions: " << HAS_SSE2 << endl;
  cout << "Processor supports SSE FP instructions: " << HAS_SSE_FP << endl;
  cout << "Processor supports SSE MMX instructions: " << HAS_SSE_MMX
            << endl;

  cout << "Processor supports 3DNow instructions: " << HAS_AMD_3DNOW
            << endl;
  cout << "Processor supports 3DNow+ instructions: " << HAS_AMD_3DNOW_PLUS
            << endl;
  cout << "IA64 processor emulating x86 : " << HAS_IA64 << endl;

  cout << "OS name: " << OS_NAME << endl;
  cout << "OS sub-type: " << OS_RELEASE << endl;
  cout << "OS build ID: " << OS_VERSION << endl;
  cout << "OS platform: " << OS_PLATFORM << endl << endl;
}

int main() {
	cout << "slow compute: " << slow_compute() << endl << endl;

	lambda();

	cout << "conditional_compile: " << conditional_compile() << endl << endl;

	processor_info();

	return 0;
}

