# text clustering

a text clustering library. 

## usage

    val sc  = new SparkContext(new SparkConf() setAppName "postclustering" setMaster "local")
    val a: util.Collection[File] =
      FileUtils.listFiles(new File(getClass.getResource("C50/C50test").toURI) ,Array("txt"),true)

    val builder = new DocBuilder(sc)
    val matrix =  builder.buildMatrix(a.asScala.toList, new EnglishTokenizer())
    val mp: Map[String, Int] = CKMeans.run(matrix, 50, 20) // it represents pairs of the paths of the files and cluster ids