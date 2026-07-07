package com.tp.springai.aimedicalpartner.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.jspecify.annotations.NonNull;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 包名称：com.tp.springai.aimedicalpartner.chatmemory
 * 类名称：FileBasedChatMemory
 * 类描述：文件会话记忆类
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/6 16:01
 */
public class FileBasedChatMemory implements ChatMemory {

    private final String BASE_DIR;

    private final static Kryo kryo = new Kryo();

    static {
        kryo.setRegistrationRequired(false);

        //设置实例化策略
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }

    /**
     * 构造对象时，指定文件保存目录
     *
     * @param baseDir 保存目录
     */
    public FileBasedChatMemory(String baseDir) {
        this.BASE_DIR = baseDir;
        File file = new File(baseDir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public void add(@NonNull String conversationId, @NonNull List<Message> messages) {
        List<Message> conversation = getOrCreateConversation(conversationId);
        conversation.addAll(messages);
        saveConversation(conversationId, conversation);
    }

    @Override
    public @NonNull List<Message> get(@NonNull String conversationId) {
        return getOrCreateConversation(conversationId);
    }

    @Override
    public void clear(@NonNull String conversationId) {
        File file = getConversationFile(conversationId);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 获取或创建会话消息的列表
     *
     * @param conversationId 会话ID
     * @return 消息列表
     */
    private List<Message> getOrCreateConversation(String conversationId) {
        File file = getConversationFile(conversationId);
        List<Message> messages = new ArrayList<>();
        if (file.exists()) {
            try (Input input = new Input(new FileInputStream(file))) {
                messages = kryo.readObject(input, ArrayList.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return messages;
    }

    /**
     * 每个会话文件单独保存
     *
     * @param conversationId 会话ID
     * @return 会话文件
     */
    private File getConversationFile(String conversationId) {
        return new File(BASE_DIR, conversationId + ".kryo");
    }

    /**
     * 保存会话消息
     *
     * @param conversationId 会话id
     * @param messages       消息列表
     */
    private void saveConversation(String conversationId, List<Message> messages) {
        File file = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))) {
            kryo.writeObject(output, messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}