# Unleashed Binding
This binding integrates with [Ruckus Unleashed Networks](https://support.ruckuswireless.com/product_families/19-ruckus-unleashed) enabling an OpenHAB instance to leverage presence detection, view  basic network information as well as blocking / unblocking clients from the network. The binding will connect to the CLI
via ssh and use an expect script style to fetch information.

## Dependencies
* jsch (osgi bundle) see: https://mvnrepository.com/artifact/org.apache.servicemix.bundles/org.apache.servicemix.bundles.jsch/0.1.55_1
* jzlib (osgi bundle) see https://mvnrepository.com/artifact/org.apache.servicemix.bundles/org.apache.servicemix.bundles.jzlib/1.1.3_2
Note, the dependencies are bundled with binding, there is no need to include them manually.

## Supported Things

* `controller` - The Ruckus Wireless Master Controller 
* `wirelessclient` - Any wireless client connected to the Ruckus Unleashed network network

## Discovery

Discovery is support and recommended. Add a new bridge thing manually and after that you can discover wireless clients by pressing the scan
button in the OpenHAB GUI. Note that only active clients on the wireless network will be discovered.

## Binding Configuration
 
The binding has no configuration options, all configuration is done at the Bridge and Thing levels.
 
## Bridge Configuration

You need at least one Master Controller (Bridge) for this binding to work. It requires a network accessible instance of the [Master Controller].
The system will use ssh and java-expect-scripts that will invoke the cli. 

The following table describes the Bridge configuration parameters:

| Parameter                | Description                                     | Config   | Default |
| ------------------------ | ----------------------------------------------  |--------- | ------- |
| host                     | Hostname or IP address of the Master Controller | Required | -       |
| port                     | Port of the for the cli                         | Required | 22      |
| username                 | The username to access the cli                  | Required | admin   |
| password                 | The password to access the cli                  | Required | -       |
| refresh                  | Refresh interval in seconds                     | Optional | 30      |

## Thing Configuration

You must define a Unleashed Master Controller (Bridge) before defining Unleashed (Things) for this binding to work.

The following table describes the Thing configuration parameters:

| Parameter    | Description                                                  | Config   | Default |
| ------------ | -------------------------------------------------------------|--------- | ------- |
| MAC Address  | The MAC address of the client                                | Required | -       |
| considerHome | The interval in seconds to consider the client as home       | Optional | 180     |

Here are some additional notes regarding the thing configuration parameters:

##### `considerHome`

The `considerHome` parameter allows you to control how quickly the binding marks a client as away. For example, using the default of `180` (seconds), the binding will report a client away as soon as `lastSeen` + `180` (seconds) < `now`

## Channels

The Wireless Client information that is retrieved is available as these channels:

| Channel ID      | Item Type | Description                                                          | Permissions |
|-----------------|-----------|--------------------------------------------------------------------- | ----------- |
| Online          | Switch    | Online status of the client                                          | Read        |
| Os              | String    | The Operating system of the client                                   | Read        |
| Host            | String    | The hostname of the client                                           | Read        |
| Mac address     | String    | MAC address of the client                                            | Read        |
| IP Address      | String    | IP address of the client                                             | Read        |
| ap              | String    | Access point (AP) the client is connected to                         | Read        |
| bssid           | String    | The Network BSSID                                                    | Read        |
| Connected since | DateTime  | The Client has been connected since this time                        | Read        |
| Auth Method     | String    | The authorization method                                             | Read        |
| Wlan            | String    | The wireless network the client is connected to                      | Read        |
| Vlan            | Number    | The vlan the client is connected to                                  | Read        |
| Channel         | Number    | The wireless channel in use                                          | Read        |
| Radio           | String    | Radio connection type (ie 802.11ac for instance)                     | Read        |
| Signal          | Number    | Signal strength                                                      | Read        |
| Status          | String    | If it is blocked/ authorized                                         | Read        |
| Last Seen       | DateTime  | When it was last seen                                                | Read        |
| Blocked         | Switch    | If the client is blocked from the network                            | Read/Write  |

### `blocked`

The `blocked` channel allows you to block / unblock a client via the controller.
The blocking will be done using an ACL. The default ACL name is "openhab". You will have to
manually assign the ACL openhab to all wireless networks. This is a step by step instruction 
to do so. Note if you don't want to use block / unblock functionality this is not needed.

* Login to unleashed web-gui
* Click on Admin & Sevices
* Click on Services
* Click on Access Control
* Click on L2/MAC Access Control
* Click on Create New
* Enter Name "openhab"
* Click only deny stations listed below
* Click "OK"
* Go back to main page
* Click WiFi-Networks
* Edit the networks where you want to the client to be able to be blocked
* Click Show Advanced Options
* Click Access Control
* For Layer2 MAC ACL select "openhab"
* Repeat for all networks where client should be blocked

## Full Example

Please add things using the OpenHAB GUI. I've kept the unleashed.things for reference.

things/unleashed.things

```
Bridge unleashed:controller:home "Unleashed Controller" [ host="unleashedhost", port=22, username="$username", password="$password", refresh=30 ] {
	Thing wirelessclient JPhone "J's Phone" [ mac="$mac", considerHome=180 ]
}
```

Replace `$user`, `$password` and `$mac` accordingly.

items/unleashed.items

```
Group    gUnleashed
Group    JPhone               "JPhone Super Android Phone"                       (gUnleashed)
Switch   JPhoneOnline         "JPhone Online [MAP(unleashed.map):%s]" (JPhone) { channel="unleashed:client:jphone:online" } 
String   JPhoneOs             "JPhone OS"                       (JPhone) { channel="unleashed:client:jphone:os" } 
String   JPhoneHost           "JPhone Host"                     (JPhone) { channel="unleashed:client:jphone:host" }
String   JPhoneMac            "JPhone Mac"                      (JPhone) { channel="unleashed:client:jphone:mac" }
String   JPhoneIp             "JPhone IP"                       (JPhone) { channel="unleashed:client:jphone:ip" }
String   JPhoneAp             "JPhone AP"                       (JPhone) { channel="unleashed:client:jphone:ap" }
String   JPhoneBssid          "JPhone Bssid"                    (JPhone) { channel="unleashed:client:jphone:bssid" }
DateTime JPhoneCS             "JPhone Connected Since [%1$tY.%1$tm.%1$td %1$tH:%1$tM]" (JPhone) { channel="unleashed:client:jphone:connected-since" }       
String   JPhoneAuthMethod     "JPhone Auth Method"              (JPhone) { channel="unleashed:client:jphone:auth-method" }
Number   JPhoneVlan             "JPhone Vlan"                   (JPhone) { channel="unleashed:client:jphone:vlan" }
String   JPhoneWlan             "JPhone Wlan"                   (JPhone) { channel="unleashed:client:jphone:wlan" }
String   JPhoneRadio             "JPhone Radio"                   (JPhone) { channel="unleashed:client:jphone:radio" }
Number   JPhoneChannel           "JPhone Wireless Channel"                   (JPhone) { channel="unleashed:client:jphone:channel" }
Number   JPhoneSignal             "JPhone Wireless Signal Strength"                   (JPhone) { channel="unleashed:client:jphone:signal" }
String   JPhoneStatus             "JPhone Status"                   (JPhone) { channel="unleashed:client:jphone:status" }
DateTime JPhoneLastSeen         "JPhone Last Seen [%1$tY.%1$tm.%1$td %1$tH:%1$tM]" (JPhone) { channel="unleashed:client:jphone:last-seen" }
Switch   JPhoneBlocked           "JPhone Blocked [%s]"       (JPhone) { channel="unleashed:client:jphone:blocked" }

```

transform/unleashed.map

```
ON=Home
OFF=Away
-=Unknown
NULL=Unknown
```

sitemaps/unleashed.sitemap

```
sitemap unleashed label="Unleashed Binding" {
	Frame {
		 Group item=gUnleashed
	}
}
```

## Changelog
### BETA11
- Removed potential memory leakage
- Updated to openHAB 3.4
### BETA10
- Major refactoring, removed external dependencies to expect and ssh.
### BETA9
- Added auto discovery
### BETA8
- OpenHAB 3.1x compatability by removing apache-commons

## Roadmap

* Reboot Single AP or Controller
* Get notification when new device is connected to guest network
* APs information
