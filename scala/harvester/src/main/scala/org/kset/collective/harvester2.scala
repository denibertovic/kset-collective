package org.kset.collective


case class Watchdog(files: Seq[String]) {
  import net.contentobjects.jnotify.{ JNotify, JNotifyListener }

  val when = JNotify.FILE_RENAMED | JNotify.FILE_DELETED | JNotify.FILE_CREATED | JNotify.FILE_MODIFIED

  val logs = files.map { f =>
    JNotify.addWatch(f, when, false, new JNotifyListener {

      def handle(name: String) = f match {
        case f => println("Activitie in file: "+ f)
      }   

      def fileRenamed(wd: Int, rootPath: String, oldName: String, newName: String) {
        handle(newName)
      }   

      def fileModified(wd: Int, rootPath: String, name: String) {
        handle(name)
      }   

      def fileDeleted(wd: Int, rootPath: String, name: String) {
        handle(name)
      }   

      def fileCreated(wd: Int, rootPath: String, name: String) {
        handle(name)
      }   
    })  
  }

  def start = logs
  def stop  = logs.foreach(JNotify.removeWatch)
}

/*object harvester extends App {
  val watchdog = new Watchdog(Seq("/var/log/syslog"))
  watchdog.start
  Thread.sleep(1000000)
  watchdog.stop


}*/


