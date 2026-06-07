package com.gestion.educativa.data.api;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class TokenManager_Factory implements Factory<TokenManager> {
  @Override
  public TokenManager get() {
    return newInstance();
  }

  public static TokenManager_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static TokenManager newInstance() {
    return new TokenManager();
  }

  private static final class InstanceHolder {
    private static final TokenManager_Factory INSTANCE = new TokenManager_Factory();
  }
}
