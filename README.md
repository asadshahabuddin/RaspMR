AUTHORS
Rahul Madhavan
Asad Shahabuddin
Sourabh Suman
Shivastuti Koul
Pulkit Jain

College of Computer and Information Science
Northeastern University
April 2015 

ABSTRACT
The conventional MapReduce API has been widely used for a multitude of jobs. It is specifically designed to analyze large amounts of data and present a quantifiable conclusion. MapReduce analysis is especially beneficial if the computation being performed is a commutative one. The pre-defined flow of a MapReduce process imposed by the conventional API can be a very good fit in certain cases and it lets the programmer focus on logical details and forget about the underlying implementation.
In this paper, we present our own version of MapReduce API. Although it takes numerous inspirations from existing APIs, we have managed to create a simpler and less specialized interface for programmers. By less specialized, it refers to the simpler prerequisites in terms of technology. RaspMR runs on any machine with Java capabilities. We have also evaluated our API’s performance and tried to present it in a lucid and contextual manner. The rest of this paper is organized to give you the details and performance of Rasp MapReduce.

INTRODUCTION
The idea is based on a simple programming interface which isn’t just easy to write code with but also to understand. Programmers can dig through the implementation details and edit the short and simple constituent classes to create a more customized framework. Moreover, we do not rely on or use a specialized distributed file system/database. Files are transferred over the network as a sequence of bytes when necessary. Intermediate files are iterated over by readers which do not need these files to be merged. It identifies patterns and creates an iterator which spans over multiple files. The most influential factors that determined our final design are:
•	Easy to create and edit
•	A good fit for classic MapReduce jobs
•	Elimination of a specialized file system
•	Obviate shuffling logic obfuscation

One of the most important design choices involved is associated with network discovery and connection. We rely on subnets to discover machines, where we designate a master and assign work to the other nodes (acting as slaves). The slave nodes only know and care about the task assigned to them. A task is a piece of the overall work that is given to one of the machines to be operated on. Tasks can be one of three types depending on whether it’s the map, reduce or shuffle phase. More details about all these phases will come in subsequent sections.
The programming model of RaspMR is expressive. Users will rely on the map() and reduce() methods to accomplish most of their tasks. Much like the conventional API, one will be expected to write a driver class and mention the other classes employed. Details about individual parts of the programming model will be explained from here onwards. Sections have been dedicated on the following constituents:
•	Network discovery
•	Network protocol for subsequent communication
•	File system handling
•	Job tracking
•	Task tracking
•	Shuffle

IMPLEMENTATION DETAILS
(1)	NETWORK DISCOVERY

(2)	COMMUNICATION PROTOCOL

(3)	FILE SYSTEM HANDLING
The file system code consists of a few important parts. The first among these is the InputFormat. The main objective of InputFormat is to calculate the parameters of each InputSplit and output them when needed. An InputSplit is a part of the overall input file. Each InputSplit deserves its own map task. The number of slaves is used to calculate the number of input splits, which is then used to determine the size of each of these splits. This results in the assignment of a starting offset and block size for each of these splits. Metadata is created in the createSplits() function. However, this is not the final metadata. Every time a split is needed, the data master calls the split() function in InputSplit class. Split creation is done using its pre-calculated dimensions and adjusted to ensure that individual lines do not cross between splits. When this chunk is received by a slave node, the re-calculated meta-information can be used to calculate default key information and other such purposes.

When a chunk of the overall input file is received at a slave node for the map phase, it needs to get the input one line at a time. This is accomplished by the RecordReader. It reads a line from the chunk and returns it with a key value. The key used is the starting offset of the split. This acts as a unique identifier in Rasp MapReduce map phase. The getCurrentKey() and getCurrentValue() methods can be utilized to get the current key and value.

The input to the reducer consists of a key and an iterator object to go over the corresponding values. This is achieved by the Iterable object. A key is provided to Iterable during object creation which is used to match file name patterns associated with the specified key. An iterator is constructed to span multiple files pertinent to the key. In all the other ways, it behaves in the same way as any other iterator.

In order to write the intermediate keys and values to the underlying file system, we needed a serializable object. RaspMR’s Writable class fits the bill perfectly. Instead of creating a number of such classes for each of the primitive types (integer, float, string, etc.), we opted in favor of a single wrapper class. Creating a new object is as simple as passing a value in any of the Java primitive types to its parameterized constructor. When the value needs to be retrieved from a Writable object, users may call the getType() method which is to be followed by getting the actual object and receiving it in a variable of that type.

Context objects are the final bits of the file system octet. There are two of these:
•	MapContext
•	ReduceContext

MapContext object is the facilitator for writing intermediate map outputs to the file system. The write() function takes two Writable paramaters – key and value. Key is used to determine the file name and to create a hash map which maintains the mapping between keys and corresponding file streams. Values for one key are appended to the end of a single file. This data is used in the shuffle phase.

ReducerContext works in much the same way except that it doesn’t create any maps. The output files are distinctly named and can be examined for final outputs.

(4)	JOB TRACKING
The user submits the job to the job tracker. The job tracker is responsible for the lifecycle management of the user’s job. A job has 4 phases in its lifecycle Map, Shuffle, Reduce and cleanup. The job tracker creates map tasks for each input split created by the file system for  the input file on which the job is to be run. In the map phase the map tasks are run on the slave machines holding the respective input splits generated by the file system. The Map tasks on completion, return the keys and the frequency of the keys generated by it to the master. This marks end of the map phase, upon which the shuffle phase of the job is initiated. The job tracker uses the output of the map tasks to decide the location (slave machines) where the reduce tasks are to be run. The slave machine having the highest frequency for a key will run the reduce task for that key. All other machines holding that key must transfer the data for that key to the machine with the highest frequency for that key. For example if m1 has the highest frequency for k1 then all other machines holding k1 would be asked to transfer the data for k1 to the m1. The shuffle task encapsulates this data transfer for keys. The job tracker generates shuffle tasks using the above mentioned logic. The shuffle phase ends when all the data transfer for the keys generated by map phase is completed. The job tracker then generates the reduce tasks for the machines holding the map data for the keys.
The output of the reduce tasks are stored on the machine where it is run. In the cleanup phase the intermediate files generated by the map, shuffle and reduce phase are deleted.    

(5)	TASK TRACKING
Task is the root interface which is inherited and implemented by all task interfaces and classes respectively. A task can be one of three types:
•	MapperTask
•	ReducerTask
•	ShuffleTask

MapperTask. MapperTask executes on a slave node and begins by setting up the mapper object and record reader using its properties. setup() and cleanup() are used to do any tasks before and after map() executes. map() is executed for every key value pair associated with the assigned input split.

ReducerTask. This class shares much of its implementation details with the MapperTask class. However, the difference lies in the way reduce() is called. reduce() gets a key and an Iterable object to iterate over all the associated values in the corresponding files.

ShuffleTask. ShuffleTask works in a different manner than the rest. Leaving the fine prints to the Shuffle section, execute() sends the map output in chunks of bytes to the worker which hosts the reduce task associated with the concerned key. Consequently, there are no setup and cleanup functions.

TaskNode. TaskNode acts like an interface for the server and client nodes for receiving and sending data and the associated task. The client and server paradigm in the case of task node is used interchangeably. The sender node becomes the client and the terminal becomes a server. At the client end, data in the form of Protocol Buffers and task are sent over the network to the desination node. At the other end, data is saved and the associated task is added to a queue. The scheduler would later poll tasks from the queue and execute them.

TaskTracker. The task tracker doesn’t do much on its own except maintaining a task queue. It pushes and polls to and from the queue.

TaskScheduler. The class is used to schedule the next task for a slave node and execute it. It behaves like a wrapper to TaskTracker.

(6)	SHUFFLE
In RaspMR, the shuffle phase begins when map phase is over, and prepares data for the reduce phase. Shuffle step is tasked with reading the map output data and identifying which machines have the maximum frequencies for each key. Using this information, grouping and partitioning takes place. 

For grouping, by default, RaspMR uses byte offset as key. This can be extended by custom implementations, as we tried and tested on a program to compute averages. The custom implementation can, just like in Hadoop, specify the keys to be used for grouping. 

For partitioning and selecting reducers, shuffle assigns the machines with maximum values for each key, as the reducer for that key. When reducers are set up in this way, network traffic and time is reduced considerably as the data which has to be sent over to the different reducers is significantly reduced for each key. However, as a downside, there can be situations when all keys might have the maximum frequencies on the same machine because of the way data is distributed in the original input file. In this scenario, all reducers will be set on the same machine and other machines will be unused, thereby leading to underutilization of resources and possibly longer run times.

Also, unlike the Hadoop MapReduce implementation, RaspMR does not sort the keys during the shuffle (shuffle and sort in Hadoop) phase. Furthermore, the partitioning in our implementation is not customizable and there is no hashing done to identify the partitions.

The map output data is stored in temporary files and a list of machine names and these files is passed on to the shuffle. The shuffle work is divided into two parts – ShuffleMaster and ShuffleSlave. ShuffleMaster manages all keys and machines along with having job information. ShuffleSlave has the task of reading map output data which master has asked to transfer to other machine and then to send this data to the concerned reducers for each key.
