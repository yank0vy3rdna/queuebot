package ru.bez_createha.queue_bot.services.impl;

import ru.bez_createha.queue_bot.dao.QueueRepository;
import ru.bez_createha.queue_bot.dao.QueueUserRepository;
import ru.bez_createha.queue_bot.model.*;
import ru.bez_createha.queue_bot.services.GroupService;
import ru.bez_createha.queue_bot.services.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueueServiceImpl implements QueueService {
    private final QueueRepository queueRepository;
    private final GroupService groupService;
    private final QueueUserRepository queueUserRepository;

    @Autowired
    public QueueServiceImpl(QueueRepository queueRepository, GroupService groupService, QueueUserRepository queueUserRepository) {
        this.queueRepository = queueRepository;
        this.groupService = groupService;
        this.queueUserRepository = queueUserRepository;
    }

    @Override
    public List<Queue> findAllByGroupId(Group groupId) {
        return queueRepository.findAllByGroupId(groupId);
    }

    @Override
    public List<Queue> findAllByStatus(QueueStatus status) {
        return queueRepository.findAllByStatus(status);
    }

    @Override
    public Queue getById(Long queue_id) {
        return queueRepository.getById(queue_id);
    }

    @Override
    public void putUser(Queue queue, User user) {
        if (queue.getQueue_users().stream().noneMatch(
                queueUser -> queueUser.getUser().getUserId().equals(user.getUserId())
        )){
            QueueUser queueUser = new QueueUser();
            queueUser.setUser(user);
            queueUser.setQueue(queue);
            queueUserRepository.save(queueUser);
        }
        save(queue);
    }

    @Override
    public void removeUser(Queue queue, User user) {
        List<QueueUser> queueUsers = queue.getQueue_users().stream().filter(
                queueUser -> queueUser.getUser().getUserId().equals(user.getUserId())
        ).collect(Collectors.toList());
        queueUserRepository.deleteAll(queueUsers);
    }

    @Override
    public void save(Queue queue) {
        Group group = groupService.findByChatId(queue.getGroupId().getChatId());
        if (group == null) {
            groupService.saveGroup(queue.getGroupId());
        }
        queueRepository.save(queue);
    }

    @Override
    public void delete(Queue queue) {
        queueUserRepository.deleteAll(queue.getQueue_users());
        queueRepository.delete(queue);
    }
}
