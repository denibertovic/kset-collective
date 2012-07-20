package org.kset.collective.harvester

import com.typesafe.config._


object HarvesterConfig {

 val config = ConfigFactory.load("harvester")
 val collective = config.getObject("collective.hostname")

 val fileConfigList = config.getObject("harvester.files")
 val fit = fileConfiList.entrySet.iterator
 val files = for(f <- fit) yield f.unwrapped
 val folders = for(file <- files) yield {
   filesplit = file.split("/")
   filesplit.take(filesplit.length - 1).reduceLeft(_+"/"+_)
 } toSet

}

