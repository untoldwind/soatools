using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;
using System.Windows.Forms;

namespace MessageListenerConsole
{
    class Program
    {
        static void Main(string[] args)
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new MessageMediator());
            /*Console.Out.WriteLine("Starting");

            Listener listener = new Listener();
            Thread thread = new Thread(listener.Run);

            thread.Start();

            Console.In.ReadLine();

            listener.Shutdown();*/
        }
    }
}
