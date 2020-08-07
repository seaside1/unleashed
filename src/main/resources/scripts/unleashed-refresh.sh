#!/usr/bin/expect
# Usage unleashed-refresh.sh <username> <password> <host> <port> <acl-list>
set username [lindex $argv 0]
set password [lindex $argv 1]
set host [lindex $argv 2]
set port [lindex $argv 3]
set list [lindex $argv 4]
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
send "show l2acl name $list\r"
expect "ruckus#"
send "show current-active-clients all\r"
expect "ruckus#"
send "quit\r"
