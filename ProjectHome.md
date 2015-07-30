# APT-Connector for M2E #

Support for Adding the generated-Folders to Java-Buildpath

## Features ##
Adding sourceOutputDirectory and outputDirectory from apt-maven-plugin to Eclipse-Buildpath.

Mark the folders as "generated", so you getting informed from eclipse when you want to change anything inside.

## In Future ##
Configure the Eclipse-Settings like the maven Configuration.

## Usage ##
The Plugin starts in the "Maven->Update Project Configuration..." cycle. (Rightclick on the Project->Maven->Update Project Configuration...).

The Plugin goes active for any Maven Project that uses apt-maven-plugin:process.

IMPORTANT: The Plugin onlys works, when you don't ignore the M2E-Connector for apt-maven-plugin. When you have added this ignore to your pom.xml's, you need to remove that.

## Installation ##

Updatesite:

http://apt-m2e.googlecode.com/git/updateSite/