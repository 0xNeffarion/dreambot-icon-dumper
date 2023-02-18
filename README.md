# DreamBot Icon Dumper

This script will dump all OSRS item's icons into a directory using the DreamBot client.

## Building

Either open the project with IntelliJ and build the artifact or run

```bash
mvn install
```

## Usage

First place the jar file (you can find it in the Releases page) in the Scripts folder inside the DreamBot directory. Then run the script.

#### With Quickstart:

```bash
java -Xmx512M -jar .\client.jar -script "com.neffware.IconDumper" -params "/target/directory"
```

#### Without Quickstart

The script will dump all the icons into the "BotData/items-icons" folder inside the DreamBot directory
