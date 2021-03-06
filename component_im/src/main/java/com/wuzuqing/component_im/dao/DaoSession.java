package com.wuzuqing.component_im.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.wuzuqing.component_im.bean.ContactsBean;
import com.wuzuqing.component_im.bean.Conversation;
import com.wuzuqing.component_im.bean.GroupBean;
import com.wuzuqing.component_im.common.packets.ChatBody;

import com.wuzuqing.component_im.dao.ContactsBeanDao;
import com.wuzuqing.component_im.dao.ConversationDao;
import com.wuzuqing.component_im.dao.GroupBeanDao;
import com.wuzuqing.component_im.dao.ChatBodyDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig contactsBeanDaoConfig;
    private final DaoConfig conversationDaoConfig;
    private final DaoConfig groupBeanDaoConfig;
    private final DaoConfig chatBodyDaoConfig;

    private final ContactsBeanDao contactsBeanDao;
    private final ConversationDao conversationDao;
    private final GroupBeanDao groupBeanDao;
    private final ChatBodyDao chatBodyDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        contactsBeanDaoConfig = daoConfigMap.get(ContactsBeanDao.class).clone();
        contactsBeanDaoConfig.initIdentityScope(type);

        conversationDaoConfig = daoConfigMap.get(ConversationDao.class).clone();
        conversationDaoConfig.initIdentityScope(type);

        groupBeanDaoConfig = daoConfigMap.get(GroupBeanDao.class).clone();
        groupBeanDaoConfig.initIdentityScope(type);

        chatBodyDaoConfig = daoConfigMap.get(ChatBodyDao.class).clone();
        chatBodyDaoConfig.initIdentityScope(type);

        contactsBeanDao = new ContactsBeanDao(contactsBeanDaoConfig, this);
        conversationDao = new ConversationDao(conversationDaoConfig, this);
        groupBeanDao = new GroupBeanDao(groupBeanDaoConfig, this);
        chatBodyDao = new ChatBodyDao(chatBodyDaoConfig, this);

        registerDao(ContactsBean.class, contactsBeanDao);
        registerDao(Conversation.class, conversationDao);
        registerDao(GroupBean.class, groupBeanDao);
        registerDao(ChatBody.class, chatBodyDao);
    }
    
    public void clear() {
        contactsBeanDaoConfig.clearIdentityScope();
        conversationDaoConfig.clearIdentityScope();
        groupBeanDaoConfig.clearIdentityScope();
        chatBodyDaoConfig.clearIdentityScope();
    }

    public ContactsBeanDao getContactsBeanDao() {
        return contactsBeanDao;
    }

    public ConversationDao getConversationDao() {
        return conversationDao;
    }

    public GroupBeanDao getGroupBeanDao() {
        return groupBeanDao;
    }

    public ChatBodyDao getChatBodyDao() {
        return chatBodyDao;
    }

}
