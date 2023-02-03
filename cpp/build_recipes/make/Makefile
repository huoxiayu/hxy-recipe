export GLOBAL_ENV_VAR="GLOBAL ENV VAR VALUE"

env: export SOME_ENV_VAR='SOME ENV VAR VALUE'

all: action1 action2 env print_global

action1:
	echo "action1"

action2:
	echo "action2"

env:
	echo "in env, SOME_ENV_VAR is: $(SOME_ENV_VAR)"

print_global: 
	echo "in print_global, SOME_ENV_VAR is: ${SOME_ENV_VAR}"
	echo "in print_global, GLOBAL_ENV_VAR is: ${GLOBAL_ENV_VAR}"
