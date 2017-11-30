# charla: Visualizando métricas con Elastic + Kibana
## Para _buildear_ el proyecto
* Requisitos:
  * Java SDK 1.8+
  * sbt: http://www.scala-sbt.org/download.html
  * git: https://git-scm.com/book/en/v2/Getting-Started-Installing-Git
```bash
git clone https://github.com/despegar/fullstacktech-2017.git
cd fullstacktech-2017
sbt compile  # va a tardar un rato para bajarse las dependencias
```

## Bajar e iniciar elasticsearch
```bash
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.0.0.tar.gz
sha1sum elasticsearch-6.0.0.tar.gz 
tar -xzf elasticsearch-6.0.0.tar.gz
cd elasticsearch-6.0.0/
bin/elasticsearch
```

## Bajar e iniciar kibana
```bash
wget https://artifacts.elastic.co/downloads/kibana/kibana-6.0.0-linux-x86_64.tar.gz
sha1sum kibana-6.0.0-linux-x86_64.tar.gz 
tar -xzf kibana-6.0.0-linux-x86_64.tar.gz
cd kibana-6.0.0-linux-x86_64/
bin/kibana
```

## Para jugar con el proyecto
```bash
$ sbt  # en el root del proyecto, para iniciar la consola sbt
sbt:flightsearchsimulator> compile
sbt:flightsearchsimulator> run
[warn] Multiple main classes detected.  Run 'show discoveredMainClasses' to see the list

Multiple main classes detected, select one to run:

 [1] example.ClientMockMain
 [2] example.ServerMain

Enter number:
```

1. __ServerMain__ es un servidor de búsquedas de vuelos _mockeado_
2. __ClientMockMain__ es un cliente del servidor actual

Para probar url válidas de búsquedas: http://localhost:8080/url-examples
