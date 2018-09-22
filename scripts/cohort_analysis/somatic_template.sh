#!/bin/bash

#
# Example gridss pipeline for somatic tumour/normal analysis
#
BLACKLIST=%(blacklist_file)s
REFERENCE=%(reference_genome)s
OUTPUT=%(patient_id)s.sv.vcf
ASSEMBLY=${OUTPUT/.sv.vcf/.gridss.assembly.bam}
GRIDSS_JAR=%(gridss_jarfile)s

if ! which bwa >/dev/null 2>&1 ; then
	echo "Missing bwa. Please add to PATH"
	exit 1
fi

if [[ ! -f "$REFERENCE" ]] ; then
	echo "Missing reference genome $REFERENCE. Update the REFERENCE variable in the shell script to your hg19 location"
	exit 1
fi

if [[ ! -f "$REFERENCE.bwt" ]] ; then
	echo "Missing bwa index for $REFERENCE. Could not find $REFERENCE.bwt. Create an index or symlink the index files to the expected file names."
	exit 1
fi

if [[ ! -f $GRIDSS_JAR ]] ; then
	echo "Missing $GRIDSS_JAR. Update the GRIDSS_JAR variable in the shell script to your location"
	exit 1
fi

if ! which java >/dev/null 2>&1 ; then
	echo "Missing java. Please add java 1.8 or later to PATH"
	exit 1
fi

JAVA_VERSION="$(java -version 2>&1 | grep version )"
if [[ ! "$JAVA_VERSION" =~ "\"1.8" ]] ; then
	echo "Detected $JAVA_VERSION. GRIDSS requires Java 1.8 or later."
	exit 1
fi

java -ea -Xmx31g \
	-Dsamjdk.create_index=true \
	-Dsamjdk.use_async_io_read_samtools=true \
	-Dsamjdk.use_async_io_write_samtools=true \
	-Dsamjdk.use_async_io_write_tribble=true \
	-Dsamjdk.compression_level=1 \
	-cp $GRIDSS_JAR gridss.CallVariants \
	TMP_DIR=. \
	WORKING_DIR=. \
	REFERENCE_SEQUENCE="$REFERENCE" \
	%(inputs)s \
	OUTPUT="$OUTPUT" \
	ASSEMBLY="$ASSEMBLY" \
	BLACKLIST="$BLACKLIST" \
	2>&1 | tee -a gridss.$HOSTNAME.$$.log
