package org.kset.collective.harvester

import net.contentobjects.jnotify.{JNotifyListener, JNotify}

object HelloWorld {
  def main(args: Array[String]) {
    // path to watch
    val path = "/home/tm/new_file";

    // watch mask, specify events you care about,
    // or JNotify.FILE_ANY for all events.
    val mask = JNotify.FILE_CREATED  |
               JNotify.FILE_DELETED  |
               JNotify.FILE_MODIFIED |
               JNotify.FILE_RENAMED;

    // watch subtree?
    val watchSubtree = true;

    // add actual watch
    val watchID = JNotify.addWatch(path, mask, watchSubtree, new Listener());

    // sleep a little, the application will exit if you
    // don’t (watching is asynchronous), depending on your
    // application, this may not be required
    Thread.sleep(1000000);

    // to remove watch the watch
    val res = JNotify.removeWatch(watchID);
    if (!res) {
      // invalid watch ID specified.
    }
  }

  class Listener extends JNotifyListener {
    def fileRenamed(wd: Int, rootPath: String, oldName: String, newName: String) {
      println("renamed " + rootPath + " : " + oldName + " -> " + newName);
    }

    def fileModified(wd: Int, rootPath: String, name: String) {
      println("modified " + rootPath + " : " + name)
    }

    def fileDeleted(wd: Int, rootPath: String, name: String) {
      println("deleted " + rootPath + " : " + name)
    }

    def fileCreated(wd: Int, rootPath: String, name: String) {
      println("created " + rootPath + " : " + name)
    }
  }
}
