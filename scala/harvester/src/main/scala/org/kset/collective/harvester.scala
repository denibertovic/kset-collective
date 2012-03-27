package org.kset.collective.harvester

import net.contentobjects.jnotify.{JNotifyListener, JNotify}
import akka.actor._
import akka.routing._

object Harvester extends App {
  sealed trait HarvesterMessage
  case object StartMaster extends HarvesterMessage
  case object StopMaster extends HarvesterMessage
  case class StartWatch(watchee: String, num: Int) extends HarvesterMessage
  case class StopWatch(watchID: Int) extends HarvesterMessage

  start

  class MasterActor extends Actor {
    val files:  Array[String] = Array("/home/tm/new_file1", "/home/tm/new_file2")
    def start = {
//      val router = context.actorOf(Props[Watcher].withRouter(RoundRobinRouter(files.size)), "watcher")
        val act1 = context.actorOf(Props[Watcher], name = "act1")
        val act2 = context.actorOf(Props[Watcher], name = "act2")

      var a = 1
      for (file <- files) {
        if (a == 1) 
          act1 ! StartWatch(file, a)
        else 
          act2 ! StartWatch(file, a)
//        router ! StartWatch(file, a)
        a += 1
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
      case StartWatch(watchee, num) => start(watchee, num)
      case StopWatch(watchID) => stop(watchID)
    }

    def start(watchee: String, num: Int) = {
      println(watchee)
      val mask = JNotify.FILE_CREATED  |
      JNotify.FILE_DELETED  |
      JNotify.FILE_MODIFIED |
      JNotify.FILE_RENAMED;

      val watchSubtree = true;

      val watchID = JNotify.addWatch(watchee, mask, watchSubtree, new Listener(num));

//      Thread.sleep(1000000);
    self ! StopWatch(watchID)
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

class Listener(num: Int) extends JNotifyListener {
  println(num)
  def fileRenamed(wd: Int, rootPath: String, oldName: String, newName: String) {
    println("renamed " + rootPath + " : " + oldName + " -> " + newName);
  }

  def fileModified(wd: Int, rootPath: String, name: String) {
    println("modified " + rootPath + " : " + name + " NUM " + num)
  }

  def fileDeleted(wd: Int, rootPath: String, name: String) {
    println("deleted " + rootPath + " : " + name)
  }

  def fileCreated(wd: Int, rootPath: String, name: String) {
    println("created " + rootPath + " : " + name)
  }
}
