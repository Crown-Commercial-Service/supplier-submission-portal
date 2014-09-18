package se.digiplant.grunt;

import play.Play;
import play.Logger;
import play.PlayPlugin;
import play.vfs.VirtualFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Run Grunt on application start in Play dev mode using "grunt run".
 * Grunt will initially check and compile/minify css and javascript files in grunt.home.
 * Grunt will check css and javascript files in grunt.home for changes in Play dev mode.
 * Run grunt dist from grunt.home in command line before deployment.
 * Set grunt.home in application.conf.
 * Toggle grunt output with grunt.log in application.conf using values of true or false.
 * Enable grunt with grunt.enabled in application.conf using values of true or false.
 * Configure Grunt using Gruntfile.js in grunt.home.
 */
public class GruntPlugin extends PlayPlugin {

	private Thread gruntThread;

    @Override
    public void onApplicationStart() {
	    if (Play.mode.isProd() || "false".equals(Play.configuration.getProperty("grunt.enabled", "false")) )
		    return;

		try {
			gruntThread = new GruntThread("grunt", "run", "--force");
			gruntThread.start();
		} catch (Exception ex) {
			Logger.error( ex, "[Grunt]" );
		}
    }

    @Override
    public void onApplicationStop() {
		if( gruntThread != null ){
			try{
				gruntThread.interrupt();
			} catch (Exception ex){}
		}
    }


	class GruntThread extends Thread {

		private Process process;
		private List<String> command;

		public GruntThread( String... commands ){
			this.command = Arrays.asList(commands);
			this.setDaemon(true);
		}

		public void interrupt(){
			if (process != null) process.destroy();
			super.interrupt();
		}

		public void run() {
			try {
				VirtualFile gruntHome = Play.getVirtualFile( Play.configuration.getProperty("grunt.home", "") );
				if( gruntHome == null ){
					Logger.error("[Grunt] unable to find Grunt home (using grunt.home in application.conf): %s", Play.configuration.getProperty("grunt.home"));
					return;
				}
				process = new ProcessBuilder(command).directory(gruntHome.getRealFile()).redirectErrorStream(false).start();

				if( "true".equals(Play.configuration.getProperty("grunt.log", "false")) ){
					InputStream in = process.getInputStream();
					StringWriter out = new StringWriter();
					String message;
					int d;
					while ((d = in.read()) != -1) {
						out.write(d);
						if( in.available() == 0 ){
							message = out.toString();
							if( message.length() > 0 ){
								String[] lines = message.split("\n");
								for( String m: lines ){
									Logger.info("[Grunt] %s", m);
								}
							}
							out = new StringWriter();
						}
					}
				}

			} catch (InterruptedIOException iex ){
				if (process != null)
					process.destroy();
			} catch (IOException ex) {
				Logger.error( ex, "[Grunt]" );
			}
		}
	}

}
