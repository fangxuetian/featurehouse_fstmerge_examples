using System; 
using System.Collections.Generic; 
using System.Text; 
using System.IO; 
using IronPython.Hosting; 
using IronPython.Compiler; namespace  MemeTracker {
	class  Program {
		
        private static  string RssBanditCacheFolder = @"C:\Documents and Settings\dareo\Local Settings\Application Data\RssBandit\Cache";
 
        private  PythonEngine engine;
 
        public  Program() {
            engine = new PythonEngine();
        }
 
        public  void Run() {
            string script = File.ReadAllText("memetracker.py");
            System.IO.FileStream fs = new System.IO.FileStream("scripting-log.txt",
              System.IO.FileMode.Create);
            engine.SetStandardOutput(fs);
            engine.SetStandardError(fs);
            engine.Execute(script);
            engine.Shutdown();
        }
 
        static  void Main(string[] args) {
            Program memetracker = new Program();
            memetracker.Run();
            Console.ReadLine();
        }

	}

}
