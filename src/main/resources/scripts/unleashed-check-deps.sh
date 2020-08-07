#!/bin/bash -
if ! command -v expect &> /dev/null
then
    echo "Expect is not installed, install using \"apt install -y expect\""
    exit 127
fi
echo "Exect is already installed!"
exit 0