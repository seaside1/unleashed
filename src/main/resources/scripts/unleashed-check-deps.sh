
#**
# * Copyright (c) 2010-2020 Contributors to the openHAB project
# *
# * See the NOTICE file(s) distributed with this work for additional
# * information.
# *
# * This program and the accompanying materials are made available under the
# * terms of the Eclipse Public License 2.0 which is available at
# * http://www.eclipse.org/legal/epl-2.0
# *
# * SPDX-License-Identifier: EPL-2.0
# */
#  @author Joseph (Seaside) Hagberg - Initial contribution
#!/bin/bash -
if ! command -v expect &> /dev/null
then
    echo "Expect is not installed, install using \"apt install -y expect\""
    exit 127
fi
echo "Expect is already installed!"
exit 0
