DescripTor -- A Tor Descriptor API for Java
===========================================

DescripTor is a Java API that fetches Tor descriptors from a variety of
sources like cached descriptors and directory authorities/mirrors.
The DescripTor API is useful to support statistical analysis of the Tor
network data and for building services and applications.

The descriptor types supported by DescripTor include relay and bridge
descriptors which are part of Tor's directory protocol as well as Torperf
data files and TorDNSEL's exit lists.  Access to these descriptors is
unified to facilitate access to publicly available data about the Tor
network.

This API is designed for Java programs that process Tor descriptors in
batches.  A Java program using this API first sets up a descriptor source
by defining where to find descriptors and which descriptors it considers
relevant.  The descriptor source then makes the descriptors available in a
descriptor store.  The program can then query the descriptor store for the
contained descriptors.  Changes to the descriptor sources after
descriptors are made available in the descriptor store will not be
noticed.  This simple programming model was designed for periodically
running, batch-processing applications and not for continuously running
applications that rely on learning about changes to an underlying
descriptor source.
