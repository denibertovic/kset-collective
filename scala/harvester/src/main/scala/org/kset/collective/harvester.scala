package org.kset.collective.harvester

import net.contentobjects.jnotify.{JNotifyListener, JNotify}
import java.io._
import akka.actor._
import akka.routing._

import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet
import scala.math._


object Harvester extends App {
  sealed trait HarvesterMessage
  case object StartMaster extends HarvesterMessage
  case object StopMaster extends HarvesterMessage
  case class StartWatch(watcheeFolder: String, watcheeFileList: HashSet[String]) extends HarvesterMessage
  case class StopWatch(watchID: Int) extends HarvesterMessage

  start

  class MasterActor extends Actor {
    val folders: Array[String] = Array("/home/tmarice/programming/kset/kset-collective/test")
    val watchList = new HashMap[String, HashSet[String]]
    watchList += "/home/tmarice/programming/kset/kset-collective/test/" -> HashSet("file1.log", "file2.log")

    def start = {
      val router = context.actorOf(Props[Watcher].withRouter(RoundRobinRouter(folders.size)), "watcher")
      for (watchPair <- watchList) {
        router ! StartWatch(watchPair._1, watchPair._2); 
      }
    }

    def stop = {
    }

    def receive = {
      case StartMaster => start
      case StopMaster => stop
    }
  }

  class Watcher extends Actor {

    def receive = {
      case StartWatch(watcheeFolder, watcheeFileList) => start(watcheeFolder, watcheeFileList)
      case StopWatch(watchID) => stop(watchID)
    }

    def start(watcheeFolder: String, watcheeFileList: HashSet[String]) = {
      val mask = JNotify.FILE_CREATED  |
      JNotify.FILE_DELETED  |
      JNotify.FILE_MODIFIED |
      JNotify.FILE_RENAMED;

      val watchSubtree = true;

      val watchID = JNotify.addWatch(watcheeFolder, mask, watchSubtree, new Listener(watcheeFolder, watcheeFileList));

    }

    def stop(watchID: Int ) = {
      val res = JNotify.removeWatch(watchID);
      if (!res) {
      }
    }

  }

  def start = {
    val system = ActorSystem("HarvesterSystem")
    val master = system.actorOf(Props[MasterActor], name="master")
    master ! StartMaster
  }

}

class Listener(watcheeFolder: String, watcheeFileList: HashSet[String]) extends JNotifyListener {
  def fileRenamed(wd: Int, rootPath: String, oldName: String, newName: String) {
    if (watcheeFileList.contains(oldName))
      println("renamed " + rootPath + " : " + oldName + " -> " + newName);
      
  }

  def fileModified(wd: Int, rootPath: String, name: String) {
    if (watcheeFileList.contains(name))
      println("modified " + rootPath + " : " + name)

      val file = new File(rootPath + "/" + name)
      val rfile = new RandomAccessFile(rootPath + "/" + name, "r")
      val pos = max(file.length() - 1024, 0)
      rfile.seek(pos)

      var line :String = "\n"
      var res  :String = ""
      line = rfile.readLine() 
      while (line != null) {
        res += line + "\n"
        line = rfile.readLine()
      }

      val lines = res.split('\n').toList
      
      println(lines.last)
  }

  def fileDeleted(wd: Int, rootPath: String, name: String) {
    if (watcheeFileList.contains(name))
      println("deleted " + rootPath + " : " + name)
  }

  def fileCreated(wd: Int, rootPath: String, name: String) {
    if (watcheeFileList.contains(name))
      println("created " + rootPath + " : " + name)
  }
}
