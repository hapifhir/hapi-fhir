# A sample Guardfile
# More info at https://github.com/guard/guard#readme

# guard 'kitchen' do
#   watch(%r{test/.+})
#   watch(%r{^recipes/(.+)\.rb$})
#   watch(%r{^attributes/(.+)\.rb$})
#   watch(%r{^files/(.+)})
#   watch(%r{^templates/(.+)})
#   watch(%r{^providers/(.+)\.rb})
#   watch(%r{^resources/(.+)\.rb})
# end

guard 'foodcritic', cookbook_paths: '.', all_on_start: false do
  watch(%r{attributes/.+\.rb$})
  watch(%r{providers/.+\.rb$})
  watch(%r{recipes/.+\.rb$})
  watch(%r{resources/.+\.rb$})
  watch('metadata.rb')
end

guard 'rubocop', all_on_start: false do
  watch(%r{attributes/.+\.rb$})
  watch(%r{providers/.+\.rb$})
  watch(%r{recipes/.+\.rb$})
  watch(%r{resources/.+\.rb$})
  watch('metadata.rb')
end

guard :rspec, cmd: 'bundle exec rspec', all_on_start: false, notification: false do
  watch(%r{^libraries/(.+)\.rb$})
  watch(%r{^spec/(.+)_spec\.rb$})
  watch(%r{^(recipes)/(.+)\.rb$})   { |m| "spec/#{m[1]}_spec.rb" }
  watch('spec/spec_helper.rb')      { 'spec' }
end
