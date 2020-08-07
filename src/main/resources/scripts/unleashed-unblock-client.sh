#!/usr/bin/expect
# Usage unleashed-unblock-client.sh <username> <password> <host> <port> <mac> <acl-list>
set username [lindex $argv 0]
set password [lindex $argv 1]
set host [lindex $argv 2]
set port [lindex $argv 3]
set mac [lindex $argv 4]
set list [lindex $argv 5]
spawn ssh $username@$host
expect {
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
send "config\r"
expect "ruckus(config)#"
send "l2acl $list\r"
expect "ruckus(config-l2acl)#"
send "del-mac $mac\r"
expect "ruckus(config-l2acl)#"
send "exit\r"
expect "ruckus(config)#"
send "exit\r"
expect "ruckus#"
send "exit\r"
expect "Exit ruckus CLI."
