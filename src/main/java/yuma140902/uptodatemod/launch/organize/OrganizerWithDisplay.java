package yuma140902.uptodatemod.launch.organize;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.swing.JDialog;
import yuma140902.uptodatemod.launch.VanillaResourceLoader;
import yuma140902.uptodatemod.launch.archives.ArchiveRegistry;
import yuma140902.uptodatemod.launch.model.Copy;
import yuma140902.uptodatemod.launch.organize.func.Functions;
import yuma140902.uptodatemod.launch.organize.func.IFunction;

public class OrganizerWithDisplay implements IOrganizer {

	private IOrganizeDisplay display;
	private Path assetsDir;
	
	public OrganizerWithDisplay(IOrganizeDisplay display, Path assetsDir) {
		this.display = display;
		this.assetsDir = assetsDir;
	}
	
	@Override
	public void organize(List<Copy> copies) throws IOException {
		JDialog dialog = this.display.getDialog();
		dialog.setVisible(true);
		
		display.setMaxProgress(copies.size());
		
		for(Copy copy : copies) {
			display.startNewCopy(copy);
			handle(copy);
		}
		
		dialog.dispose();
	}
	
	private void handle(Copy copy) throws IOException {
		String archiveId = copy.ar;
		FileSystem archive = ArchiveRegistry.getArchive(archiveId);
		Path src = archive.getPath("assets/minecraft/").resolve(copy.src);
		Path dest = this.assetsDir.resolve(copy.dest);
		String func = copy.func;
		
		if(!Files.exists(src)) {
			VanillaResourceLoader.log.error(String.format("%s was not found. Skipping.", src));
			return;
		}
		
		Files.createDirectories(dest.getParent());
		if(Files.exists(dest)) {
			
		}
		
		IFunction function;
		if(func == null || "".equals(func)) {
			function = Functions.onlyCopy;
		}
		else if("rotate90".equals(func)) {
			function = Functions.rotate90;
		}
		else if("rotate180".equals(func)) {
			function = Functions.rotate180;
		}
		else if("rotate270".equals(func)) {
			function = Functions.rotate270;
		}
		else if("flip".equals(func)) {
			function = Functions.flip;
		}
		else {
			VanillaResourceLoader.log.error(String.format("Skipping with unknown function %s", func));
			function = Functions.onlyCopy;
		}
		
		function.handle(src, dest);
		
		VanillaResourceLoader.log.info(String.format("%s -> %s", src, dest));
	}
	
}
