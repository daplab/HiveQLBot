Hive Queries from HipChat
====

[ChatOps](http://blogs.atlassian.com/2016/01/what-is-chatops-adoption-guide/) is huge these days
<sup>[[1](http://sdtimes.com/chatops-is-taking-over-enterprises/)]</sup>.
[DAPLAB](http://daplab.ch) is no exception to that trend. Concretely, we're using
[HipChat](https://www.hipchat.com/) during our [HackyThursday](http://daplab.ch/#hacky). 

To integrate ChatOps with Data, the idea of the project was to allow anyone running
[Hive](https://hive.apache.org/) queries from our [HipChat team room](https://daplab.hipchat.com/chat/room/2390200)

![Demo time :) ](images/2.png =480px)

# HipChat Custom Integration

HipChat allows the creation of _so-called_ slash commands to quickly notify any REST endpoint. See
[https://blog.hipchat.com/2015/02/11/build-your-own-integration-with-hipchat/](https://blog.hipchat.com/2015/02/11/build-your-own-integration-with-hipchat/)
for more details about this integration feature.

In the HipChat admin page, creating a slash command integration is really easy, and requires
to define the name of the command, `/query` in our case, as well as an endpoint which will
be requested at every `/query` message

![Integration with hiveqlbot](images/1.png =480px)

## Message format

The request sent to the REST contains a JSON payload looking like that:

```
{
    "event": "room_message",
    "item": {
        "message": {
            "date": "2016-04-28T20:21:51.866984+00:00",
            "from": {
                "id": 3450167,
                "links": {
                    "self": "https://api.hipchat.com/v2/user/3450167"
                },
                "mention_name": "BenoitPerroud",
                "name": "Benoit Perroud",
                "version": "00000000"
            },
            "id": "2b7b3ba8-d379-4e9c-91f8-bb332decb078",
            "mentions": [],
            "message": "/sql select * from bperroud.chobachoba_raw_logs where year = 2016 and month = 3 LIMIT 10",
            "type": "message"
        },
        "room": {
            "id": 2390200,
            "is_archived": false,
            "links": {
                "participants": "https://api.hipchat.com/v2/room/2390200/participant",
                "self": "https://api.hipchat.com/v2/room/2390200",
                "webhooks": "https://api.hipchat.com/v2/room/2390200/webhook"
            },
            "name": "HackyThursday",
            "privacy": "public",
            "version": "UA25ZO8M"
        }
    },
    "oauth_client_id": "b80f3c24-76ce-442a-9499-33dd895ccebf",
    "webhook_id": 4694751
}
```

# Architecture

The architecture is pretty straight forward:

1) A REST endpoint which will receive the message. We'll use [SparkJava](http://sparkjava.com/) for that part.
2) A service which will parse and process the Hive query. We'll add [bonecp](http://www.jolbox.com/) connection pool to add resiliency.
3) A service which will post the answer back to HipChat. [Hipchat-java](https://github.com/evanwong/hipchat-java) will be used here.

## Development Cycle

In order to get HipChat message to our development environment, we used 
[Ngrok)[https://ngrok.com]. This service allows you to make internal,
development port publicly available on the internet.

```
./ngrok http 4567
```
_Note: port 4567 is the default port for [SparkJava](http://sparkjava.com/)_

We need to remap the HipChat integration to the temporary enpoint

![ngrock time :) ](images/2.png =480px)

# Try it out!

## Build it

```
mvn clean install
```

This will generate a RPM. `rpm -ivh` to install it.

## Configure it

Get the `apitoken` for your hipchat integration and create a application.conf file in /opt/daplab/hive-ql-bot/config. 
An example is given [here](src/main/config/application.conf.example).

## Launch it

A convenience script is given to [run HiveQLBot](src/main/bin/run.sh).

```
/opt/daplab/hive-ql-bot/bin/run.sh
```

Just mind that Hive will run with the permissions of the user launching the `run.sh` script,
and hence will be allowed to query all the tables this user has access to.

## Live demo

Login into our [HipChat team room](https://daplab.hipchat.com/chat/room/2390200) and try to run a query!

The HiveQLBOt project is hosted with ❤ by the [DAPLAB](http://daplab.ch) team.
