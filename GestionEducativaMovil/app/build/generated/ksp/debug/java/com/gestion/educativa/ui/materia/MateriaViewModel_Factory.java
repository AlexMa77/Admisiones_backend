package com.gestion.educativa.ui.materia;

import com.gestion.educativa.data.api.TokenManager;
import com.gestion.educativa.data.repository.MateriaRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class MateriaViewModel_Factory implements Factory<MateriaViewModel> {
  private final Provider<MateriaRepository> repositoryProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public MateriaViewModel_Factory(Provider<MateriaRepository> repositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public MateriaViewModel get() {
    return newInstance(repositoryProvider.get(), tokenManagerProvider.get());
  }

  public static MateriaViewModel_Factory create(Provider<MateriaRepository> repositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new MateriaViewModel_Factory(repositoryProvider, tokenManagerProvider);
  }

  public static MateriaViewModel newInstance(MateriaRepository repository,
      TokenManager tokenManager) {
    return new MateriaViewModel(repository, tokenManager);
  }
}
