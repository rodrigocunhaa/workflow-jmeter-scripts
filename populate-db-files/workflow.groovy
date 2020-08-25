companyId = com.liferay.portal.kernel.util.PortalUtil.getCompanyId(actionRequest);

group = com.liferay.portal.kernel.service.GroupServiceUtil.getGroup(companyId, "Guest");

groupId = group.getGroupId();

userId = com.liferay.portal.kernel.util.PortalUtil.getUserId(actionRequest);

for (i =0; i < 1000; i++) {
    boolean autoPassword = true;
    String password = "123";
    String screenName = i + "screenName";
    String emailAddress = i + "test@liferay.com";
    long facebookId = 0;
    String openId = "";
    String firstName = i + "firstName";
    String middleName = "";
    String lastName = i + "lastName";
    long prefixId = 0;
    long suffixId = 0;
    boolean male = true;
    int birthdayMonth = 1;
    int birthdayDay = 1;
    int birthdayYear = 1970;
    String jobTitle = "";
    long[] organizationIds = null;
    long[] roleIds = null;
    long[] userGroupIds = null;
    boolean sendMail = false;
    long[] groudIds = [groupId];

    serviceContext = new com.liferay.portal.kernel.service.ServiceContext();
    serviceContext.setCompanyId(companyId);
    serviceContext.setUserId(userId);

    groupUser = com.liferay.portal.kernel.service.UserLocalServiceUtil.addUser(
        0L, companyId, autoPassword, password, password,
        false, screenName, emailAddress, facebookId,
        openId, java.util.Locale.US, firstName, middleName, lastName, prefixId, suffixId,
        male, birthdayMonth, birthdayDay, birthdayYear, jobTitle, groudIds,
        organizationIds, roleIds, userGroupIds, sendMail, serviceContext);

    role = com.liferay.portal.kernel.service.RoleLocalServiceUtil.getRole(companyId, "Administrator");

    long [] userIds = [groupUser.getUserId()];

    com.liferay.portal.kernel.service.UserLocalServiceUtil.addRoleUser(role.getRoleId(), groupUser);

    com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil.addUserGroupRoles(userIds, groupId, role.getRoleId());
}

com.liferay.portal.kernel.service.WorkflowDefinitionLinkLocalServiceUtil.updateWorkflowDefinitionLink(
    userId, companyId, groupId, com.liferay.blogs.model.BlogsEntry.class.getName(), 0, 0, "Load Test", 1);

serviceContext = new com.liferay.portal.kernel.service.ServiceContext();

serviceContext.setCompanyId(companyId)
serviceContext.setScopeGroupId(groupId)
serviceContext.setUserId(userId)

for (int i = 0; i < 10000; i++) {
    com.liferay.blogs.service.BlogsEntryLocalServiceUtil.addEntry(userId, "Blog " + i, "Blog "+ i, serviceContext);
}

workflowTasks = com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.getWorkflowTasksByUserRoles(
    companyId, userId, false, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, null);
        
for (com.liferay.portal.kernel.workflow.WorkflowTask workflowTask : workflowTasks) {
    com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.assignWorkflowTaskToUser(
        companyId, userId, workflowTask.getWorkflowTaskId(), userId, com.liferay.petra.string.StringPool.BLANK, null, null);
}

workflowTasks =  com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.getWorkflowTasksByUser(
    companyId, userId, false, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, null);

for (com.liferay.portal.kernel.workflow.WorkflowTask workflowTask : workflowTasks) {
     com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.completeWorkflowTask(
        companyId, userId, workflowTask.getWorkflowTaskId(), com.liferay.portal.kernel.util.Constants.APPROVE, com.liferay.petra.string.StringPool.BLANK, null);
}

for (int i = 10000; i < 20000; i++) {
    com.liferay.blogs.service.BlogsEntryLocalServiceUtil.addEntry(userId, "Blog " + i, "Blog "+ i, serviceContext);
}

workflowTasks = com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.getWorkflowTasksByUserRoles(
    companyId, userId, false, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, null);
        
for (com.liferay.portal.kernel.workflow.WorkflowTask workflowTask : workflowTasks) {
    com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.assignWorkflowTaskToUser(
        companyId, userId, workflowTask.getWorkflowTaskId(), userId, com.liferay.petra.string.StringPool.BLANK, null, null);

}

workflowTasks =  com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.getWorkflowTasksByUser(
    companyId, userId, false, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, null);

for (com.liferay.portal.kernel.workflow.WorkflowTask workflowTask : workflowTasks) {
     com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.completeWorkflowTask(
        companyId, userId, workflowTask.getWorkflowTaskId(), com.liferay.portal.kernel.util.Constants.APPROVE, com.liferay.petra.string.StringPool.BLANK, null);
}

for (int i = 20000; i < 30000; i++) {
    com.liferay.blogs.service.BlogsEntryLocalServiceUtil.addEntry(userId, "Blog " + i, "Blog "+ i, serviceContext);
}

workflowTasks = com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.getWorkflowTasksByUserRoles(
    companyId, userId, false, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, null);
        
for (com.liferay.portal.kernel.workflow.WorkflowTask workflowTask : workflowTasks) {
    com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.assignWorkflowTaskToUser(
        companyId, userId, workflowTask.getWorkflowTaskId(), userId, com.liferay.petra.string.StringPool.BLANK, null, null);

}

workflowTasks =  com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.getWorkflowTasksByUser(
    companyId, userId, false, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, null);

for (com.liferay.portal.kernel.workflow.WorkflowTask workflowTask : workflowTasks) {
     com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.completeWorkflowTask(
        companyId, userId, workflowTask.getWorkflowTaskId(), com.liferay.portal.kernel.util.Constants.APPROVE, com.liferay.petra.string.StringPool.BLANK, null);
}

for (int i = 30000; i < 40000; i++) {
    com.liferay.blogs.service.BlogsEntryLocalServiceUtil.addEntry(userId, "Blog " + i, "Blog "+ i, serviceContext);
}

workflowTasks = com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.getWorkflowTasksByUserRoles(
    companyId, userId, false, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, null);
        
for (com.liferay.portal.kernel.workflow.WorkflowTask workflowTask : workflowTasks) {
    com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.assignWorkflowTaskToUser(
        companyId, userId, workflowTask.getWorkflowTaskId(), userId, com.liferay.petra.string.StringPool.BLANK, null, null);

}

workflowTasks =  com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.getWorkflowTasksByUser(
    companyId, userId, false, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, null);

for (com.liferay.portal.kernel.workflow.WorkflowTask workflowTask : workflowTasks) {
     com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.completeWorkflowTask(
        companyId, userId, workflowTask.getWorkflowTaskId(), com.liferay.portal.kernel.util.Constants.APPROVE, com.liferay.petra.string.StringPool.BLANK, null);
}


for (int i = 40000; i < 50000; i++) {
    com.liferay.blogs.service.BlogsEntryLocalServiceUtil.addEntry(userId, "Blog " + i, "Blog "+ i, serviceContext);
}

workflowTasks = com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.getWorkflowTasksByUserRoles(
    companyId, userId, false, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, null);
        
for (com.liferay.portal.kernel.workflow.WorkflowTask workflowTask : workflowTasks) {
    com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.assignWorkflowTaskToUser(
        companyId, userId, workflowTask.getWorkflowTaskId(), userId, com.liferay.petra.string.StringPool.BLANK, null, null) ;

}

workflowTasks =  com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.getWorkflowTasksByUser(
    companyId, userId, false, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, null);

for (com.liferay.portal.kernel.workflow.WorkflowTask workflowTask : workflowTasks) {
     com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.completeWorkflowTask(
        companyId, userId, workflowTask.getWorkflowTaskId(), com.liferay.portal.kernel.util.Constants.APPROVE, com.liferay.petra.string.StringPool.BLANK, null);
}

workflowTasks = com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.getWorkflowTasksByUserRoles(
    companyId, userId, false, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, null);
        
for (com.liferay.portal.kernel.workflow.WorkflowTask workflowTask : workflowTasks) {
    com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.assignWorkflowTaskToUser(
        companyId, userId, workflowTask.getWorkflowTaskId(), userId, com.liferay.petra.string.StringPool.BLANK, null, null) ;

}

workflowTasks =  com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.getWorkflowTasksByUser(
    companyId, userId, false, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, null);

for (com.liferay.portal.kernel.workflow.WorkflowTask workflowTask : workflowTasks) {
     com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.completeWorkflowTask(
        companyId, userId, workflowTask.getWorkflowTaskId(), com.liferay.portal.kernel.util.Constants.APPROVE, com.liferay.petra.string.StringPool.BLANK, null);
}

for (int i = 50000; i < 100000; i++) {
    com.liferay.blogs.service.BlogsEntryLocalServiceUtil.addEntry(userId, "Blog " + i, "Blog "+ i, serviceContext);
}

workflowTasks =  com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.getWorkflowTasksByUser(
    companyId, userId, false, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, com.liferay.portal.kernel.dao.orm.QueryUtil.ALL_POS, null);

for (com.liferay.portal.kernel.workflow.WorkflowTask workflowTask : workflowTasks) {
     com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil.completeWorkflowTask(
        companyId, userId, workflowTask.getWorkflowTaskId(), com.liferay.portal.kernel.util.Constants.APPROVE, com.liferay.petra.string.StringPool.BLANK, null);
}

