# -*- coding: utf-8 -*-
from elasticsearch import Elasticsearch, helpers
import datetime

portalIndex = "liferay-20097"
processIndex = portalIndex + "-workflow-metrics-processes"
nodeIndex = portalIndex + "-workflow-metrics-nodes"
instanceIndex = portalIndex + "-workflow-metrics-instances"
taskIndex = portalIndex + "-workflow-metrics-tasks"
pendingInstances = 100000
completedInstances = 100000


def unix_time_millis(myDateTime):
    res = (datetime.datetime(myDateTime.year, myDateTime.month, myDateTime.day, myDateTime.hour,
                             myDateTime.minute, myDateTime.second) - datetime.datetime(1970, 1, 1)).total_seconds()
    return res


es = Elasticsearch([
    {'host': 'localhost', 'port': 9201}
])


def getDefinition():
    companyId = 0
    processId = 0
    nodes = {}
    userId = 0
    fullName = ""
    version = "1.0"

    res = es.search(index=processIndex, body={"query": {
        "bool": {
            "must": [
                {
                    "match": {
                        "name": "Single Approver"
                    }
                }
            ]
        }
    }})
    for hits in res['hits']['hits']:
        companyId = hits['_source']['companyId']
        processId = hits['_source']['processId']
        version = hits['_source']['version']

        res = es.search(index=nodeIndex, body={"query": {
            "bool": {
                "must": [
                    {
                        "term": {
                            "version": {
                                "value": version,
                                "boost": 1
                            }
                        }
                    },
                    {
                        "term": {
                            "companyId": {
                                "value": companyId,
                                "boost": 1
                            }
                        }
                    },
                    {
                        "term": {
                            "processId": {
                                "value": processId,
                                "boost": 1
                            }
                        }
                    }
                ]
            }
        }})
        for hits in res['hits']['hits']:
            nodes[hits['_source']['name']] = hits['_source']['nodeId']

    res = es.search(index=portalIndex, body={"query": {
        "bool": {
            "must": [
                {
                    "match": {
                        "screenName": "test"
                    }
                }
            ]
        }
    }})
    for hits in res['hits']['hits']:
        userId = hits['_source']['userId']
        fullName = hits['_source']['fullName']

    return companyId, processId, userId, nodes, fullName, version


companyId, processId, userId, nodes, fullName, version = getDefinition()

createDate = datetime.datetime.utcnow() - datetime.timedelta(minutes=3)
completionDate = createDate + datetime.timedelta(minutes=3)

instanceDocs = [
    {
        "_id": 'WorkflowMetricsInstance' + str(doc),
        "_source": {
            "assetTitle_en_US": "Dummy " + str(doc),
            "assetTitle_en_US_sortable": "Dummy " + str(doc),
            "assetType_en_US": "Dummy",
            "assetType_en_US_sortable": "Dummy",
            "className": "Dummy",
            "classPK": doc,
            "companyId": companyId,
            "completed": False,
            "createDate": createDate.strftime('%Y%m%d%H%M%S'),
            "createDate_Number_sortable": unix_time_millis(createDate),
            "deleted": False,
            "instanceId": doc,
            "modifiedDate": createDate.strftime('%Y%m%d%H%M%S'),
            "processId": processId,
            "uid": "WorkflowMetricsInstance" + str(doc),
            "userId": userId,
            "userName": fullName,
            "version": version,
        }
    }
    for doc in range(1, pendingInstances+1)
]

taskDocs = [
    {
        "_id": 'WorkflowMetricsTask' + str(doc),
        "_source": {
            "assetTitle_en_US": "Dummy " + str(doc),
            "assetTitle_en_US_sortable": "Dummy " + str(doc),
            "assetType_en_US": "Dummy",
            "assetType_en_US_sortable": "Dummy",
            "assigneeIds": userId,
            "assigneeType": "com.liferay.portal.kernel.model.User",
            "className": "Dummy",
            "classPK": doc,
            "companyId": companyId,
            "completed": False,
            "createDate": createDate.strftime('%Y%m%d%H%M%S'),
            "createDate_Number_sortable": unix_time_millis(createDate),
            "deleted": False,
            "instanceCompleted": False,
            "instanceId": doc,
            "modifiedDate": createDate.strftime('%Y%m%d%H%M%S'),
            "name": "review",
            "nodeId": nodes["review"],
            "processId": processId,
            "taskId": doc,
            "uid": "WorkflowMetricsTask" + str(doc),
            "userId": userId,
            "version": version
        }
    }
    for doc in range(1, pendingInstances+1)
]

instanceCompletedDocs = [
    {
        "_id": 'WorkflowMetricsInstance' + str(doc),
        "_source": {
            "assetTitle_en_US": "Dummy " + str(doc),
            "assetTitle_en_US_sortable": "Dummy " + str(doc),
            "assetType_en_US": "Dummy",
            "assetType_en_US_sortable": "Dummy",
            "className": "Dummy",
            "classPK": doc,
            "companyId": companyId,
            "completed": True,
            "completionDate": completionDate.strftime('%Y%m%d%H%M%S'),
            "completionDate_Number_sortable": unix_time_millis(completionDate),
            "createDate": createDate.strftime('%Y%m%d%H%M%S'),
            "createDate_Number_sortable": unix_time_millis(createDate),
            "deleted": False,
            "duration": int((completionDate.timestamp() - createDate.timestamp()) * 1000),
            "instanceId": doc,
            "modifiedDate": completionDate.strftime('%Y%m%d%H%M%S'),
            "processId": processId,
            "uid": "WorkflowMetricsInstance" + str(doc),
            "userId": userId,
            "userName": fullName,
            "version": version,
        }
    }
    for doc in range(pendingInstances+1, pendingInstances + completedInstances+1)
]

completionDate = createDate + datetime.timedelta(minutes=1)

taskReviewCompletedDocs1 = [
    {
        "_id": 'WorkflowMetricsTask' + str(doc),
        "_source": {
            "assetTitle_en_US": "Dummy " + str(doc),
            "assetTitle_en_US_sortable": "Dummy " + str(doc),
            "assetType_en_US": "Dummy",
            "assetType_en_US_sortable": "Dummy",
            "assigneeIds": userId,
            "assigneeType": "com.liferay.portal.kernel.model.User",
            "className": "Dummy",
            "classPK": doc,
            "companyId": companyId,
            "completed": True,
            "completionDate": completionDate.strftime('%Y%m%d%H%M%S'),
            "completionUserId": userId,
            "createDate": createDate.strftime('%Y%m%d%H%M%S'),
            "createDate_Number_sortable": unix_time_millis(createDate),
            "deleted": False,
            "duration": int((completionDate.timestamp() - createDate.timestamp()) * 1000),
            "instanceCompleted": True,
            "instanceCompletionDate": completionDate.strftime('%Y%m%d%H%M%S'),
            "instanceId": doc,
            "modifiedDate": completionDate.strftime('%Y%m%d%H%M%S'),
            "name": "review",
            "nodeId": nodes["review"],
            "processId": processId,
            "taskId": doc,
            "uid": "WorkflowMetricsTask" + str(doc),
            "userId": userId,
            "version": version
        }
    }
    for doc in range(pendingInstances+1, pendingInstances + completedInstances+1)
]

createDate = completionDate
completionDate = createDate + datetime.timedelta(minutes=1)

taskUpdateCompletedDocs = [
    {
        "_id": 'WorkflowMetricsTask' + str(doc),
        "_source": {
            "assetTitle_en_US": "Dummy " + str(doc),
            "assetTitle_en_US_sortable": "Dummy " + str(doc),
            "assetType_en_US": "Dummy",
            "assetType_en_US_sortable": "Dummy",
            "assigneeIds": userId,
            "assigneeType": "com.liferay.portal.kernel.model.User",
            "className": "Dummy",
            "classPK": doc,
            "companyId": companyId,
            "completed": True,
            "completionDate": completionDate.strftime('%Y%m%d%H%M%S'),
            "completionUserId": userId,
            "createDate": createDate.strftime('%Y%m%d%H%M%S'),
            "createDate_Number_sortable": unix_time_millis(createDate),
            "deleted": False,
            "duration": int((completionDate.timestamp() - createDate.timestamp()) * 1000),
            "instanceCompleted": True,
            "instanceCompletionDate": completionDate.strftime('%Y%m%d%H%M%S'),
            "instanceId": doc,
            "modifiedDate": completionDate.strftime('%Y%m%d%H%M%S'),
            "name": "update",
            "nodeId": nodes["update"],
            "processId": processId,
            "taskId": doc,
            "uid": "WorkflowMetricsTask" + str(doc),
            "userId": userId,
            "version": version
        }
    }
    for doc in range(pendingInstances + completedInstances+1, pendingInstances + completedInstances + completedInstances+1)
]

createDate = completionDate
completionDate = createDate + datetime.timedelta(minutes=1)

taskReviewCompletedDocs2 = [
    {
        "_id": 'WorkflowMetricsTask' + str(doc),
        "_source": {
            "assetTitle_en_US": "Dummy " + str(doc),
            "assetTitle_en_US_sortable": "Dummy " + str(doc),
            "assetType_en_US": "Dummy",
            "assetType_en_US_sortable": "Dummy",
            "assigneeIds": userId,
            "assigneeType": "com.liferay.portal.kernel.model.User",
            "className": "Dummy",
            "classPK": doc,
            "companyId": companyId,
            "completed": True,
            "completionDate": completionDate.strftime('%Y%m%d%H%M%S'),
            "completionUserId": userId,
            "createDate": createDate.strftime('%Y%m%d%H%M%S'),
            "createDate_Number_sortable": unix_time_millis(createDate),
            "deleted": False,
            "duration": int((completionDate.timestamp() - createDate.timestamp()) * 1000),
            "instanceCompleted": True,
            "instanceCompletionDate": completionDate.strftime('%Y%m%d%H%M%S'),
            "instanceId": doc,
            "modifiedDate": completionDate.strftime('%Y%m%d%H%M%S'),
            "name": "review",
            "nodeId": nodes["review"],
            "processId": processId,
            "taskId": doc,
            "uid": "WorkflowMetricsTask" + str(doc),
            "userId": userId,
            "version": version
        }
    }
    for doc in range(pendingInstances + completedInstances + completedInstances+1, pendingInstances + completedInstances + completedInstances + completedInstances+1)
]

helpers.bulk(es, instanceDocs, index=instanceIndex)
helpers.bulk(es, taskDocs, index=taskIndex)
helpers.bulk(es, instanceCompletedDocs, index=instanceIndex)
helpers.bulk(es, taskReviewCompletedDocs1, index=taskIndex)
helpers.bulk(es, taskUpdateCompletedDocs, index=taskIndex)
helpers.bulk(es, taskReviewCompletedDocs2, index=taskIndex)
