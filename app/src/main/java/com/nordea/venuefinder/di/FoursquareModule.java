package com.nordea.venuefinder.di;

import com.nordea.venuefinder.BuildConfig;
import com.nordea.venuefinder.api.FoursquareService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class FoursquareModule {

  @Singleton
  @Provides
  public static FoursquareService provideFoursquareService(
    OkHttpClient httpClient
  ) {
    return new Retrofit.Builder()
      .baseUrl(BuildConfig.FOURSQUARE_API_URL)
      .client(httpClient)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
      .build()
      .create(FoursquareService.class);
  }

  @Singleton
  @Provides
  public static OkHttpClient provideOkHttpClient(
    HttpLoggingInterceptor httpLoggingInterceptor
  ) {
    return new OkHttpClient().newBuilder()
      .addInterceptor(httpLoggingInterceptor)
      .connectTimeout(15, TimeUnit.SECONDS)
      .readTimeout(15, TimeUnit.SECONDS)
      .writeTimeout(15, TimeUnit.SECONDS)
      .build();
  }

  @Singleton
  @Provides
  public static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    loggingInterceptor.setLevel(BuildConfig.DEBUG
      ? HttpLoggingInterceptor.Level.BODY
      : HttpLoggingInterceptor.Level.NONE
    );

    return loggingInterceptor;
  }
}
