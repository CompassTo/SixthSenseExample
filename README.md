# Sixth Sense SDK - Usage and code example

## Synopsis

Sixth Sense SDK allows to create robust proximity based applications using LTE Direct technology on Android. It is based on LTE Direct expression handling introduced by Qualcomm’s QDiscovery SDK.

## Motivation

Our goal is to change the way people make sense of their smartphones. Proximity technologies allow the device to identify how close it is to something, whereas location knows where the device is. We like to think of proximity as the needed layer of relevance to location-based technology. Because while location provides limited relevance for the user the objective behind proximity is to provide contextual experiences. We are on the mission to add new technologies to apps, make them easy to use and support them with powerful services.

## Usage

To understand the basic functionality of LTE Direct we recommend to read Qualcomm’s QDiscovery SDK documentation: 
[https://ltedirect.qualcomm.com/resources/docs/lte-d-supplemental-sdk-docs](https://ltedirect.qualcomm.com/resources/docs/lte-d-supplemental-sdk-docs)

The Sixth Sense SDK manages the complex LTE Direct transaction workflow for subscribe/publish/match of expressions.
It combines expressions into stand-alone ‚Tasks’ which can create, destroy and update multiple asynchronous transactions. Each Task is running independently and is designed to represent a certain feature within your application (e.g. car2car, people discovery, messaging). Therefore all expressions created within a Task use the same expression prefix.

The Tasks are separated into Public Managed, Public Unmanaged and Private functionality (based on Transaction Types).
Additionally each Task is split into Publish and Subscribe functionality (based on Publish and Subscribe expressions)
Publish Tasks are used to broadcast feature related data. Subscribe Tasks define and handle the incoming data for a feature. 

The current implementation of the SDK includes only Public Managed expression handling. (see Planned Features)

It provides Public Managed Subscribe and Publish Tasks that use Qualcomm’s ENS for expression match handling. Each task covers multiple match keys which affects the assignment of incoming expressions.

## Code Example

```java
LtedConfig config = new LtedConfig(getApplicationContext(),
ApplicationConfig.LTED_APP_ID);

List<LtedMatchKey> keys = new ArrayList<LtedMatchKey>();
List<String> musicLabels = new ArrayList();

LtedMatchKey musicKey = new LtedMatchKey("Music");
musicKey.addLabel("rock");
musicKey.addLabel("electro");
keys.add(musicKey);
final PublicManagedSubscribeTask subscribeTask = new
PublicManagedSubscribeTask(this, DataProvider.LTED_DISCOVERY_TASK);
subscribeTask.setMatchKeys(keys);

LtedManager.initialize(config, new LtedManager.ILtedManagerCallbackHandler(){
    @Override
    public void onInitSuccess (){
        subscribeTask.start();
    }
});
```

## Installation

1.	Download Sixth Sense SDK and copy ’sixthsense_VERSION.aar’ file into your project/lib directory
2.	Download QDiscovery SDK (currently at v.0.9.3) and copy ’qdiscovery_VERSION.jar’ file into your project/lib directory:
[https://ltedirect.qualcomm.com/resources/sdk/093-combined-lte-direct-sdk-package-qce](https://ltedirect.qualcomm.com/resources/sdk/093-combined-lte-direct-sdk-package-qce)
3.	Update your build.gradle file:

```java
compile(name:'sixthsense_0.9.0', ext:'aar')
compile files('libs/qdiscoverysdk_0.9.3.jar')
compile 'com.google.code.gson:gson:2.5.0'
```

4.  Configure your API-Key:

```java
LtedPublicManagedConfigModel config = new LtedPublicManagedConfigModel(
  "LTED_APP_ID", // Have to be an unique ID like "com.company.appname"
  "ENS-PROJECT-ADDRESS", // e.g. publicExpr.dynamicMngd.PROJECT-X
  "API-KEY");
```

To run your application without an LTE Direct capability you need to install LTE Direct simulator on your device. See Qualcomm support document for install instructions: [https://ltedirect.qualcomm.com/resources/docs/discovery-install-configuration](https://ltedirect.qualcomm.com/resources/docs/discovery-install-configuration)
Besides you need to register any device on which you want to run the application on Qualcomm’s developer platform: [https://ltedirect.qualcomm.com/devices/assigned-devices](https://ltedirect.qualcomm.com/devices/assigned-devices)

You also need to create a project on Qualcomm developer platform that you can rely on in your application: [https://ltedirect.qualcomm.com/dynamic_managed](https://ltedirect.qualcomm.com/dynamic_managed)
 (See Qualcomm Supporting Doc: [https://ltedirect.qualcomm.com/resources/docs/lte-d-supplemental-sdk-docs](https://ltedirect.qualcomm.com/resources/docs/lte-d-supplemental-sdk-docs) : 1_LTED_BuildingPublicManagedexpressions.pdf )

To use Public Managed Tasks create a Dynamic Managed application / project on the platform. There you register unique project specific arguments that are referenced within your application. Any expression match will base on the keys that you set in your Public Managed Tasks.
Each Key contains a static ENS argument defined within the online platform project and dynamic labels that can be defined within the application itself (even at runtime).

## API Reference

Please review the docs folder within the Sixth Sense SDK project.

## Sixth Sense Platform

The Sixth Sense SDK collects data and send it to the platform. There you can see whether expressions were successful or canceled due to an error.

[http://sixthsense.compass.to](http://sixthsense.compass.to)

## Planned Features 

As for now the SDK handles Public Managed expressions. Currently we are working on Private and Public Unmanaged expression functionality. The referenced gson library that is used to parse SSI data in Public Managed Tasks will be replaced by an internal JsonParser.

## Contributors

[http://proximity.compass.to](http://proximity.compass.to)
Let people know how they can dive into the project, include important links to things like issue trackers, irc, twitter accounts if applicable.

## License

Example code that is hosted here: MIT

Sixth Sense SDK: Copyright 2015 Hugleberry Corp.
