#include <string>

struct A_INTERFACE;

A_INTERFACE *new_A_INTERFACE(int32_t a, int64_t b, bool c, std::string d);

void print_A_INTERFACE(A_INTERFACE *a_interface);

void delete_A_INTERFACE(A_INTERFACE *a_interface);
