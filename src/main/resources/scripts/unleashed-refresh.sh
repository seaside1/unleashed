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
#!/usr/bin/expect
# Usage unleashed-refresh.sh <username> <password> <host> <port> <acl-list>
set username [lindex $argv 0]
set password [lindex $argv 1]
set host [lindex $argv 2]
set port [lindex $argv 3]
set list [lindex $argv 4]
spawn ssh $username@$host
expect -timeout 1 {
	"Are you sure you want to continue connecting" {
                send "yes\r"
        }
}
expect "Please login:"
send "$username\r"
expect "Password:"
send "$password\r"
expect "ruckus>"
send "enable\r"
expect "ruckus#"
send "show l2acl name $list\r"
expect "ruckus#"
send "show current-active-clients all\r"
expect "ruckus#"
send "quit\r"
