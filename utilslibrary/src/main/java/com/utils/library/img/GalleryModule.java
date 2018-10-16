package com.utils.library.img;


import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
import com.utils.library.R;

import java.io.InputStream;

import androidx.annotation.NonNull;

/**
 * @author cuiyang
 */
@GlideModule
public final class GalleryModule extends AppGlideModule {
    // Intentionally empty.

    /**
     * 为了避免检查元数据（和相关的错误）的性能开销，您可以在迁移完成后通过覆盖以下方法来禁用清单解析AppGlideModule：
     *
     * @return
     */
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));
//        setMemoryBigSize(context, builder);
//        ViewTarget.setTagId(R.id.glide_tag_id);
    }
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide,
                                   @NonNull Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }

    /**
     * 设置内存缓存使用的内存最大缓存数
     * 每个手机可能分配的不同 先获取默认的值再*百分再设置
     */
    private void setMemoryBigSize(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).build();
        int customMemoryCacheSize = (int) (.7f * calculator.getMemoryCacheSize());//默认的再加70%
//        int customBitmapPoolSize = (int) (1.5 * defaultBitmapPoolSize);
        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
//        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));//设置缓存区池的大小
    }


    /**
     * 设置自定义目录外面可访问
     */
    private void setDiskCacheDirectory(GlideBuilder builder) {
        int cacheSize100MegaBytes = 104857600;//磁盘缓存最大100M大小
// or any other path
        String downloadDirectoryPath = Environment.getDownloadCacheDirectory().getPath();
//        builder.setDiskCache(
//                new DiskLruCacheFactory(downloadDirectoryPath, cacheSize100MegaBytes)
//        );
        builder.setDiskCache(
                new DiskLruCacheFactory(downloadDirectoryPath, "glidecache", cacheSize100MegaBytes)
        );
    }

    /**
     * 设置磁盘缓存到sd卡上但非指定路径
     *
     * @param context
     * @param builder
     */
    private void setDiskCacheOfSdcard(Context context, GlideBuilder builder) {
        // set size & external vs. internal
        int cacheSize100MegaBytes = 104857600;//磁盘缓存最大100M大小

        builder.setDiskCache(
                new ExternalCacheDiskCacheFactory(context, cacheSize100MegaBytes));
    }

    /**
     * 设置磁盘缓存到app内部目录下但非指定路径
     *
     * @param context
     * @param builder
     */
    private void setDisCacheOfApp(Context context, GlideBuilder builder) {
        // set size & external vs. internal
        int cacheSize100MegaBytes = 104857600;//磁盘缓存最大100M大小
        builder.setDiskCache(
                new InternalCacheDiskCacheFactory(context, cacheSize100MegaBytes)
        );
    }
}
