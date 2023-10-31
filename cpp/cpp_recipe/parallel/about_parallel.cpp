// g++ about_parallel.cpp -std=c++17 -ltbb
#include <chrono>
#include <iostream>
#include <algorithm>
#include <vector>
#include <execution>
#include <thread>
#include <atomic>

void foo() {
	std::vector<std::string> foo;
	foo.push_back("1");
	foo.push_back("2");
	foo.push_back("3");
	foo.push_back("4");

	for (int i = 0; i < 10; i++) {
		std::for_each(
			std::execution::par,
			foo.begin(),
			foo.end(),
			[](auto&& item)
			{
				std::this_thread::sleep_for (std::chrono::seconds(1));
				std::cout << std::this_thread::get_id() << " -> " << item << std::endl;
			}
		);

		std::this_thread::sleep_for (std::chrono::seconds(1));

		std::cout << "************" << std::endl;
	}
}

void bar() {
	int64_t len = 1000;
	std::vector<int64_t> bar;
	for (int64_t i = 1; i <= len; i++) {
		bar.push_back(i);
	}

	std::atomic_int64_t sum1{0};
	auto start1 = std::chrono::steady_clock::now();
	std::for_each(
			std::execution::seq,
			bar.begin(),
			bar.end(),
			[&](auto&& item)
			{
				std::this_thread::sleep_for (std::chrono::milliseconds(1));
				sum1 += item;
			}
	);
	auto end1 = std::chrono::steady_clock::now();
	auto millis1 = std::chrono::duration_cast<std::chrono::milliseconds>(end1 - start1).count();
	std::cout << "cost1 " << millis1 << " ms" << std::endl;
	std::cout << "sum1 -> " << sum1 << std::endl;

	std::atomic_int64_t sum2{0};
	auto start2 = std::chrono::steady_clock::now();
	std::for_each(
			std::execution::par,
			bar.begin(),
			bar.end(),
			[&](auto&& item)
			{
				std::this_thread::sleep_for (std::chrono::milliseconds(1));
				sum2 += item;
			}
	);
	auto end2 = std::chrono::steady_clock::now();
	auto millis2 = std::chrono::duration_cast<std::chrono::milliseconds>(end2 - start2).count();
	std::cout << "cost2 " << millis2 << " ms" << std::endl;
	std::cout << "sum2 -> " << sum2 << std::endl;
}

void biz() {
	std::vector<int64_t> biz;
	int len = 1000;
	for (int i = 1; i <= len; i++) {
		biz.push_back(i);
	}
	for (auto&& item0 : biz) {
		std::cout << "item0 : " << item0 << std::endl;
	}

	std::vector<int64_t> out1(biz.size(), 0);
	std::vector<int64_t> out2(biz.size(), 0);

	auto start1 = std::chrono::steady_clock::now();
	std::transform(
			std::execution::seq,
			biz.begin(),
			biz.end(),
			out1.begin(),
			[](auto&& input_item)
			{
				std::this_thread::sleep_for (std::chrono::milliseconds(1));
				return input_item * 2;
			}
	);
	auto end1 = std::chrono::steady_clock::now();
	auto millis1 = std::chrono::duration_cast<std::chrono::milliseconds>(end1 - start1).count();
	std::cout << "cost1 " << millis1 << " ms" << std::endl;
	for (auto&& item1 : out1) {
		std::cout << "item1 : " << item1 << std::endl;
	}

	auto start2 = std::chrono::steady_clock::now();
	std::transform(
			std::execution::par,
			biz.begin(),
			biz.end(),
			out2.begin(),
			[](auto&& input_item)
			{
				std::this_thread::sleep_for (std::chrono::milliseconds(1));
				return input_item * 2;
			}
	);
	auto end2 = std::chrono::steady_clock::now();
	auto millis2 = std::chrono::duration_cast<std::chrono::milliseconds>(end2 - start2).count();
	std::cout << "cost2 " << millis2 << " ms" << std::endl;
	for (auto&& item2 : out2) {
		std::cout << "item2 : " << item2 << std::endl;
	}
}

int main() {

	foo();
	bar();
	biz();

	return 0;

}
