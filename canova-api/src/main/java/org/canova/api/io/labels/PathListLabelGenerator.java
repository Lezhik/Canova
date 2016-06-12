package org.canova.api.io.labels;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.canova.api.io.data.Text;
import org.canova.api.writable.Writable;

/**
 * Create labels from directory names into root dir.
 *
 * @author Lezhik
 */
public class PathListLabelGenerator implements PathLabelGenerator {
	protected String[] locations;
	
	public PathListLabelGenerator(File rootDir) {
		ArrayList<String> paths = new ArrayList<String>();
		for(File child: rootDir.listFiles()) {
			if (child.isDirectory()) {
				paths.add(FilenameUtils.normalize(child.getAbsolutePath()));
			}
		}
		locations = new String[paths.size()];
		paths.toArray(locations);
	}
	
	@Override
	public Writable getLabelForPath(String path) {
		path = FilenameUtils.normalize(path);
		for(int i = 0; i < locations.length; i++) {
			try {
				if (FilenameUtils.directoryContains(locations[i], path)) {
					return new Text(locations[i]);
				}
			} catch (IOException e) {
				throw new RuntimeException("Path checking error: " + locations[i], e);
			}
		}
		throw new IllegalArgumentException("Wrong path: " + path);
	}

	@Override
	public Writable getLabelForPath(URI uri) {
        return getLabelForPath(new File(uri).toString());
	}

}
