lib_dir=./lastfm-lib

# Find the path to the file.jar file within the lastfm-lib directory
jar_path=$(find "$lib_dir" -name "lastfm-java-0.1.3-SNAPSHOT.jar" -print -quit)

# Check if the file.jar file was found
if [[ -n $jar_path ]]; then
  # Run mvn install:install-file with the appropriate arguments
  mvn install:install-file -Dfile="$jar_path" -DgroupId=de.u-mass -DartifactId=lastfm-java -Dversion=0.1.3 -Dpackaging=jar
else
  echo "lastfm-library not found within $lib_dir"
fi